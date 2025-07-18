package com.example.biblio.service;

import com.example.biblio.model.Adherant;
import com.example.biblio.model.Pret;
import com.example.biblio.model.Prolongement;
import com.example.biblio.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ProlongementService {

    private static final Logger logger = LoggerFactory.getLogger(ProlongementService.class);

    @Autowired
    private ProlongementRepository prolongementRepository;

    @Autowired
    private AdherantRepository adherantRepository;

    @Autowired
    private PretRepository pretRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private AbonnementRepository abonnementRepository;

    // Récupérer toutes les demandes de prolongement en attente
    public List<Prolongement> getAllPendingProlongements() {
        return prolongementRepository.findAllPendingProlongements();
    }

    @Transactional
    public void demanderProlongement(Integer idPret, Integer idAdherant) {
        logger.info("Demande de prolongement pour prêt id={} par adhérant id={}", idPret, idAdherant);

        // Vérifier l'existence du prêt
        Pret pret = pretRepository.findById(idPret)
                .orElseThrow(() -> new IllegalArgumentException("Prêt non trouvé"));

        // Vérifier que le prêt appartient à l'adhérant
        if (!pret.getAdherant().getIdAdherant().equals(idAdherant)) {
            throw new IllegalStateException("Ce prêt n'appartient pas à cet adhérant");
        }

        // Vérifier que le prêt est en cours
        if (pret.getDateRetourReelle() != null) {
            throw new IllegalStateException("Le prêt est déjà terminé");
        }

        // Récupérer l'adhérant
        Adherant adherant = adherantRepository.findById(idAdherant)
                .orElseThrow(() -> new IllegalArgumentException("Adhérant non trouvé"));

        // Vérifier les règles de gestion
        if (adherant.getQuotaRestantProlongement() <= 0) {
            throw new IllegalStateException("Quota de prolongements dépassé");
        }
        if (pret.getNombreProlongements() >= adherant.getTypeAdherant().getQuotaProlongements()) {
            throw new IllegalStateException("Nombre maximal de prolongements atteint pour ce prêt");
        }
        if (reservationRepository.hasActiveReservationBeforeDate(pret.getExemplaire().getId(), LocalDateTime.now())) {
            throw new IllegalStateException("Prolongement non autorisé, exemplaire réservé");
        }
        if (abonnementRepository.hasActivePenalite(adherant.getIdAdherant(), LocalDate.now())) {
            throw new IllegalStateException("Adhérant sanctionné");
        }
        if (!adherant.hasValidAbonnement(LocalDate.now())) {
            throw new IllegalStateException("Abonnement non valide");
        }
        // Vérifier qu'il n'y a pas de demande de prolongement en attente pour ce prêt
        List<Prolongement> prolongementsEnAttente = prolongementRepository.findByPretIdAndStatut(
                idPret, Prolongement.StatutProlongement.EN_ATTENTE);
        if (!prolongementsEnAttente.isEmpty()) {
            throw new IllegalStateException("Une demande de prolongement est déjà en attente pour ce prêt");
        }

        // Calculer la nouvelle date de retour en utilisant jours_prets du type d'adhérant
        int joursProlongement = adherant.getTypeAdherant().getJoursPrets();
        LocalDate nouvelleDateRetour = pret.getDateRetourPrevue().plusDays(joursProlongement);

        // Créer la demande de prolongement
        Prolongement prolongement = new Prolongement(
                pret,
                adherant,
                pret.getExemplaire(),
                LocalDateTime.now(),
                nouvelleDateRetour
        );
        prolongement.setStatut(Prolongement.StatutProlongement.EN_ATTENTE);
        prolongementRepository.save(prolongement);

        logger.info("Demande de prolongement créée : id={}, prêt id={}, nouvelle date de retour={}, durée prolongement={} jours",
                prolongement.getId(), idPret, nouvelleDateRetour, joursProlongement);
    }

    @Transactional
    public void approuverProlongement(Integer idProlongement) {
        Prolongement prolongement = prolongementRepository.findById(idProlongement)
                .orElseThrow(() -> new IllegalArgumentException("Demande de prolongement non trouvée"));

        if (prolongement.getStatut() != Prolongement.StatutProlongement.EN_ATTENTE) {
            throw new IllegalStateException("La demande de prolongement n'est pas en attente");
        }

        // Récupérer l'adhérant
        Adherant adherant = adherantRepository.findById(prolongement.getAdherant().getIdAdherant())
                .orElseThrow(() -> new IllegalArgumentException("Adhérant non trouvé"));

        // Vérifier les règles de gestion
        // 1. L'adhérant doit exister
        // (Déjà vérifié par findById)

        // 2. L'exemplaire doit être actuellement emprunté par cet adhérant
        Pret pret = pretRepository.findByAdherantIdAndExemplaireIdAndDateRetourReelleIsNull(
                prolongement.getAdherant().getIdAdherant(),
                prolongement.getExemplaire().getId());
        if (pret == null) {
            throw new IllegalStateException("L'exemplaire n'est pas actuellement emprunté par cet adhérant");
        }

        // 3. Le nombre de prolongements autorisés n'est pas dépassé
        if (adherant.getQuotaRestantProlongement() <= 0) {
            throw new IllegalStateException("Quota de prolongements dépassé");
        }
        if (pret.getNombreProlongements() >= adherant.getTypeAdherant().getQuotaProlongements()) {
            throw new IllegalStateException("Nombre maximal de prolongements atteint pour ce prêt");
        }

        // 4. Aucune réservation antérieure à la date de demande de prolongement ne doit exister
        if (reservationRepository.hasActiveReservationBeforeDate(prolongement.getExemplaire().getId(), prolongement.getDateDemande())) {
            throw new IllegalStateException("Prolongement non autorisé, exemplaire réservé avant la demande de prolongement");
        }

        // 5. L'adhérant ne doit pas avoir de sanction active
        if (abonnementRepository.hasActivePenalite(adherant.getIdAdherant(), LocalDate.now())) {
            throw new IllegalStateException("Adhérant sanctionné");
        }

        // 6. L'abonnement de l'adhérant doit être valide
        if (!adherant.hasValidAbonnement(LocalDate.now())) {
            throw new IllegalStateException("Abonnement non valide");
        }

        // Calculer la nouvelle date de retour : date_retour_prevue actuelle + jours_prets
        int joursProlongement = adherant.getTypeAdherant().getJoursPrets();
        LocalDate nouvelleDateRetour = pret.getDateRetourPrevue().plusDays(joursProlongement);

        // Vérifier que la nouvelle_date_retour_prevue dans Prolongement correspond
        if (!prolongement.getNouvelleDateRetourPrevue().equals(nouvelleDateRetour)) {
            logger.warn("La nouvelle date de retour proposée ({}) ne correspond pas à la règle (+{} jours: {}). Mise à jour avec la date calculée.",
                    prolongement.getNouvelleDateRetourPrevue(), joursProlongement, nouvelleDateRetour);
            prolongement.setNouvelleDateRetourPrevue(nouvelleDateRetour);
        }

        // Mise à jour du prêt
        pret.setDateRetourPrevue(nouvelleDateRetour);
        pret.setNombreProlongements(pret.getNombreProlongements() + 1);
        pretRepository.save(pret);

        // Mise à jour du quota de prolongements
        adherant.setQuotaRestantProlongement(adherant.getQuotaRestantProlongement() - 1);
        adherantRepository.save(adherant);

        // Mise à jour du statut de la demande
        prolongement.setStatut(Prolongement.StatutProlongement.APPROUVE);
        prolongementRepository.save(prolongement);

        logger.info("Prolongement approuvé : id={}, nouvelle date de retour={}, durée prolongement={} jours", 
                    idProlongement, nouvelleDateRetour, joursProlongement);
    }

    @Transactional
    public void refuserProlongement(Integer idProlongement) {
        Prolongement prolongement = prolongementRepository.findById(idProlongement)
                .orElseThrow(() -> new IllegalArgumentException("Demande de prolongement non trouvée"));

        if (prolongement.getStatut() != Prolongement.StatutProlongement.EN_ATTENTE) {
            throw new IllegalStateException("La demande de prolongement n'est pas en attente");
        }

        // Mise à jour du statut de la demande
        prolongement.setStatut(Prolongement.StatutProlongement.REFUSE);
        prolongementRepository.save(prolongement);

        logger.info("Prolongement refusé : id={}", idProlongement);
    }
}