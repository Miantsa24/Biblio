package com.example.biblio.service;

import com.example.biblio.model.Adherant;
import com.example.biblio.model.Exemplaire;
import com.example.biblio.model.Livre;
import com.example.biblio.model.Pret;
import com.example.biblio.model.Reservation;
import com.example.biblio.repository.AdherantRepository;
import com.example.biblio.repository.AbonnementRepository;
import com.example.biblio.repository.ExemplaireRepository;
import com.example.biblio.repository.LivreRepository;
import com.example.biblio.repository.PretRepository;
import com.example.biblio.repository.ReservationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class ReservationService {

    private static final Logger logger = LoggerFactory.getLogger(ReservationService.class);

    @Autowired
    private AdherantRepository adherantRepository;

    @Autowired
    private AbonnementRepository abonnementRepository;

    @Autowired
    private ExemplaireRepository exemplaireRepository;

    @Autowired
    private LivreRepository livreRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private PretRepository pretRepository;

    @Transactional
    public void reserverLivre(Integer idAdherant, Integer idExemplaire, LocalDate dateReservation) {
        Adherant adherant = adherantRepository.findById(idAdherant)
                .orElseThrow(() -> new IllegalArgumentException("Adhérant non trouvé"));

        if (!adherant.hasValidAbonnement(dateReservation)) {
            throw new IllegalStateException("Abonnement non valide");
        }

        if (abonnementRepository.hasActivePenalite(idAdherant, dateReservation)) {
            throw new IllegalStateException("Adhérant sanctionné");
        }

        Exemplaire exemplaire = exemplaireRepository.findById(idExemplaire)
                .orElseThrow(() -> new IllegalArgumentException("Exemplaire non trouvé"));

        Livre livre = livreRepository.findById(exemplaire.getIdLivre())
                .orElseThrow(() -> new IllegalArgumentException("Livre non trouvé"));

        int ageAdherant = (int) ChronoUnit.YEARS.between(adherant.getDateNaissance(), dateReservation);
        if (livre.getAgeMinimum() > ageAdherant) {
            throw new IllegalStateException("Livre non adapté à l'âge de l'adhérant");
        }

        if (adherant.getQuotaRestantReservation() <= 0) {
            throw new IllegalStateException("Quota de réservations dépassé");
        }

        // Vérifier si un exemplaire du livre est disponible
        List<Exemplaire> exemplaires = exemplaireRepository.findByLivreId(livre.getId());
        boolean hasAvailableExemplaire = exemplaires.stream()
                .anyMatch(e -> e.getStatut() == Exemplaire.StatutExemplaire.DISPONIBLE);
        if (hasAvailableExemplaire) {
            logger.info("Réservation refusée pour adhérant id={} et exemplaire id={} : un exemplaire est disponible.", idAdherant, idExemplaire);
            throw new IllegalStateException("Un exemplaire de ce livre est disponible. Veuillez emprunter directement.");
        }

        // Vérifier la date de réservation par rapport à la date de retour prévue
        Pret pretEnCours = pretRepository.findByExemplaireIdAndDateRetourReelleIsNull(idExemplaire);
        if (pretEnCours != null && dateReservation.isBefore(pretEnCours.getDateRetourPrevue())) {
            String errorMessage = String.format(
                "La réservation pour l'exemplaire %d est impossible car il ne sera disponible qu'à partir du %s.",
                idExemplaire, pretEnCours.getDateRetourPrevue()
            );
            logger.info(errorMessage);
            throw new IllegalStateException(errorMessage);
        }

        Reservation reservation = new Reservation();
        reservation.setIdAdherant(idAdherant);
        reservation.setIdExemplaire(idExemplaire);
        reservation.setDateReservation(dateReservation);
        reservation.setDateDemande(LocalDateTime.now());
        reservation.setStatut(Reservation.StatutReservation.EN_ATTENTE);

        reservationRepository.save(reservation);
        logger.info("Réservation créée pour adhérant id={} et exemplaire id={}, date_demande={}", 
                    idAdherant, idExemplaire, reservation.getDateDemande());
    }

    @Transactional
    public void approuverReservation(Integer idReservation) {
        Reservation reservation = reservationRepository.findById(idReservation)
                .orElseThrow(() -> new IllegalArgumentException("Réservation non trouvée"));

        if (reservation.getStatut() != Reservation.StatutReservation.EN_ATTENTE) {
            throw new IllegalStateException("La réservation n'est pas en attente");
        }

        Adherant adherant = adherantRepository.findById(reservation.getIdAdherant())
                .orElseThrow(() -> new IllegalArgumentException("Adhérant non trouvé"));

        // Vérifier et décrémenter le quota
        if (adherant.getQuotaRestantReservation() <= 0) {
            throw new IllegalStateException("Quota de réservations dépassé");
        }

        // Vérifier la date de réservation par rapport à la date de retour prévue
        Pret pretEnCours = pretRepository.findByExemplaireIdAndDateRetourReelleIsNull(reservation.getIdExemplaire());
        if (pretEnCours != null && reservation.getDateReservation().isBefore(pretEnCours.getDateRetourPrevue())) {
            reservation.setStatut(Reservation.StatutReservation.ANNULEE);
            reservationRepository.save(reservation);
            String errorMessage = String.format(
                "La réservation pour l'exemplaire %d est annulée car il ne sera disponible qu'à partir du %s.",
                reservation.getIdExemplaire(), pretEnCours.getDateRetourPrevue()
            );
            throw new IllegalStateException(errorMessage);
        }

        int ancienQuota = adherant.getQuotaRestantReservation();
        adherant.setQuotaRestantReservation(ancienQuota - 1);
        adherantRepository.save(adherant);
        logger.info("Quota de réservations décrémenté pour adhérant id={} : {} -> {}", 
                    adherant.getIdAdherant(), ancienQuota, adherant.getQuotaRestantReservation());

        reservation.setStatut(Reservation.StatutReservation.HONOREE);
        reservationRepository.save(reservation);

        // Mettre à jour le statut de l'exemplaire à RESERVE si ce n'est pas déjà EMPRUNTE
        Exemplaire exemplaire = exemplaireRepository.findById(reservation.getIdExemplaire())
                .orElseThrow(() -> new IllegalArgumentException("Exemplaire non trouvé"));
        if (exemplaire.getStatut() != Exemplaire.StatutExemplaire.EMPRUNTE) {
            exemplaire.setStatut(Exemplaire.StatutExemplaire.RESERVE);
            exemplaireRepository.save(exemplaire);
        }
    }

    @Transactional
    public void refuserReservation(Integer idReservation) {
        Reservation reservation = reservationRepository.findById(idReservation)
                .orElseThrow(() -> new IllegalArgumentException("Réservation non trouvée"));

        if (reservation.getStatut() != Reservation.StatutReservation.EN_ATTENTE) {
            throw new IllegalStateException("La réservation n'est pas en attente");
        }

        reservation.setStatut(Reservation.StatutReservation.ANNULEE);
        reservationRepository.save(reservation);

        Exemplaire exemplaire = exemplaireRepository.findById(reservation.getIdExemplaire())
                .orElseThrow(() -> new IllegalArgumentException("Exemplaire non trouvé"));
        boolean hasOtherReservations = reservationRepository.hasActiveReservation(exemplaire.getId());
        if (!hasOtherReservations && exemplaire.getStatut() == Exemplaire.StatutExemplaire.RESERVE) {
            exemplaire.setStatut(Exemplaire.StatutExemplaire.DISPONIBLE);
            exemplaireRepository.save(exemplaire);
        }
    }

    public List<Reservation> getAllPendingReservations() {
        return reservationRepository.findAllPendingReservations();
    }
}