
package com.example.biblio.service;

import com.example.biblio.model.*;
import com.example.biblio.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class PretService {

    private static final Logger logger = LoggerFactory.getLogger(PretService.class);

    @Autowired
    private AdherantRepository adherantRepository;

    @Autowired
    private LivreRepository livreRepository;

    @Autowired
    private ExemplaireRepository exemplaireRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private PretRepository pretRepository;

    @Autowired
    private PenaliteRepository penaliteRepository;

    @Autowired
    private JourFerierRepository jourFerierRepository;

    @Transactional
    public void preterLivre(Integer idAdherant, Integer idLivre, String typePret, LocalDate datePret) {
        logger.info("Tentative de prêt : idAdherant={}, idLivre={}, typePret={}, datePret={}", 
                    idAdherant, idLivre, typePret, datePret);

        // Vérifications initiales
        Adherant adherant = adherantRepository.findById(idAdherant)
                .orElseThrow(() -> new IllegalArgumentException("Adhérant non trouvé"));
        if (!adherant.hasValidAbonnement(datePret)) {
            throw new IllegalStateException("Abonnement non valide");
        }
        if (penaliteRepository.existsByAdherantIdAdherantAndDateFinPenaliteAfter(idAdherant, datePret)) {
            throw new IllegalStateException("Adhérant sanctionné");
        }
        Livre livre = livreRepository.findById(idLivre)
                .orElseThrow(() -> new IllegalArgumentException("Livre non trouvé"));
        int ageAdherant = (int) ChronoUnit.YEARS.between(adherant.getDateNaissance(), datePret);
        if (livre.getAgeMinimum() > ageAdherant) {
            throw new IllegalStateException("Livre non adapté à l'âge de l'adhérant");
        }
        if (pretRepository.countByAdherantIdAdherantAndDateRetourReelleIsNull(idAdherant) >= adherant.getQuotaRestant()) {
            throw new IllegalStateException("Quota de prêts dépassé");
        }
        if (!typePret.equals("A_EMPORTER") && !typePret.equals("SUR_PLACE")) {
            throw new IllegalArgumentException("Type de prêt invalide");
        }

        // Recherche d'un exemplaire disponible ou réservé
        List<Exemplaire> exemplaires = exemplaireRepository.findByLivreId(idLivre);
        if (exemplaires.isEmpty()) {
            throw new IllegalStateException("Aucun exemplaire disponible pour ce livre");
        }

        // Annuler les réservations expirées
        for (Exemplaire exemplaire : exemplaires) {
            Reservation reservation = reservationRepository.findFirstActiveReservationByExemplaireId(exemplaire.getId());
            if (reservation != null && reservation.getStatut() == Reservation.StatutReservation.HONOREE && 
                datePret.isAfter(reservation.getDateReservation())) {
                reservation.setStatut(Reservation.StatutReservation.ANNULEE);
                reservationRepository.save(reservation);
                exemplaire.setStatut(Exemplaire.StatutExemplaire.DISPONIBLE);
                exemplaireRepository.save(exemplaire);
                logger.info("Réservation {} annulée pour l'exemplaire {}, statut changé à DISPONIBLE", 
                            reservation.getId(), exemplaire.getId());
            }
        }

        // Trouver un exemplaire disponible ou réservé pour l'adhérant
        Exemplaire exemplaireChoisi = null;
        for (Exemplaire exemplaire : exemplaires) {
            if (exemplaire.getStatut() == Exemplaire.StatutExemplaire.DISPONIBLE) {
                exemplaireChoisi = exemplaire;
                break;
            }
            if (exemplaire.getStatut() == Exemplaire.StatutExemplaire.RESERVE) {
                Reservation topReservation = reservationRepository.findTopPriorityReservationByExemplaireId(exemplaire.getId());
                if (topReservation != null && topReservation.getIdAdherant().equals(idAdherant) && 
                    !datePret.isAfter(topReservation.getDateReservation())) {
                    exemplaireChoisi = exemplaire;
                    break;
                }
            }
        }

        if (exemplaireChoisi == null) {
            throw new IllegalStateException("Aucun exemplaire disponible ou réservé pour cet adhérant");
        }

        // Vérifier si l'exemplaire est réservé pour un autre adhérant
        Reservation topReservation = reservationRepository.findTopPriorityReservationByExemplaireId(exemplaireChoisi.getId());
        if (exemplaireChoisi.getStatut() == Exemplaire.StatutExemplaire.RESERVE && 
            (topReservation == null || !topReservation.getIdAdherant().equals(idAdherant))) {
            throw new IllegalStateException("L'exemplaire est réservé pour un autre adhérant");
        }

        // Créer le prêt
        int joursPrets = adherant.getTypeAdherant().getJoursPrets();
        LocalDate dateRetourPrevue = datePret.plusDays(joursPrets);
        while (jourFerierRepository.existsByDateFerier(dateRetourPrevue)) {
            dateRetourPrevue = dateRetourPrevue.plusDays(1);
        }

        Pret pret = new Pret();
        pret.setAdherant(adherant);
        pret.setExemplaire(exemplaireChoisi);
        pret.setDatePret(datePret);
        pret.setDateRetourPrevue(dateRetourPrevue);
        pret.setTypePret(Pret.TypePret.valueOf(typePret));
        pret.setNombreProlongements(0);


        // Mettre à jour le statut de l'exemplaire et de la réservation
        exemplaireChoisi.setStatut(Exemplaire.StatutExemplaire.EMPRUNTE);
        if (topReservation != null && topReservation.getIdAdherant().equals(idAdherant)) {
            topReservation.setStatut(Reservation.StatutReservation.TERMINEE);
            reservationRepository.save(topReservation);
            // Restaurer le quota de réservation
            int ancienQuotaReservation = adherant.getQuotaRestantReservation();
            adherant.setQuotaRestantReservation(ancienQuotaReservation + 1);
            logger.info("Quota de réservations incrémenté pour adhérant id={} : {} -> {}", 
                        idAdherant, ancienQuotaReservation, adherant.getQuotaRestantReservation());
        }

        // Mettre à jour le quota de l'adhérant pour le prêt
        int ancienQuotaPret = adherant.getQuotaRestant();
        adherant.setQuotaRestant(ancienQuotaPret - 1);
        adherantRepository.save(adherant);
        exemplaireRepository.save(exemplaireChoisi);
        pretRepository.save(pret);

        logger.info("Prêt créé : idAdherant={}, idExemplaire={}, datePret={}, dateRetourPrevue={}, joursPrets={}", 
                    idAdherant, exemplaireChoisi.getId(), datePret, dateRetourPrevue, joursPrets);
    }
}
