package com.example.biblio.service;

import com.example.biblio.model.*;
import com.example.biblio.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.List;

@Service
public class RendreLivreService {

    private static final Logger logger = LoggerFactory.getLogger(RendreLivreService.class);
    private static final int NOMBRE_JOURS_PENALITE = 7; // Pénalité fixée à 7 jours en cas de retard

    @Autowired
    private AdherantRepository adherantRepository;

    @Autowired
    private ExemplaireRepository exemplaireRepository;

    @Autowired
    private PretRepository pretRepository;

    @Autowired
    private PenaliteRepository penaliteRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private JourFerierRepository jourFerierRepository;

    @Autowired
    private ProlongementRepository prolongementRepository;

    public LocalDate rendreLivre(Integer idAdherant, Integer idExemplaire, LocalDate dateRetourReelle) {
        logger.info("Début du rendu du livre pour idAdherant={}, idExemplaire={}, dateRetourReelle={}", 
                    idAdherant, idExemplaire, dateRetourReelle);

        // Vérification de l'existence de l'adhérant
        Adherant adherant = adherantRepository.findById(idAdherant)
                .orElseThrow(() -> new IllegalArgumentException("Adhérant non trouvé"));

        // Vérification de l'existence de l'exemplaire
        Exemplaire exemplaire = exemplaireRepository.findById(idExemplaire)
                .orElseThrow(() -> new IllegalArgumentException("Exemplaire non trouvé"));

        // Vérification si l'exemplaire est emprunté par cet adhérant
        Pret pret = pretRepository.findByAdherantIdAndExemplaireIdAndDateRetourReelleIsNull(idAdherant, idExemplaire);
        if (pret == null) {
            throw new IllegalStateException("Exemplaire non emprunté par cet adhérant");
        }

        // Vérifier la cohérence avec les prolongements approuvés
        List<Prolongement> prolongementsApprouves = prolongementRepository.findByPretIdAndStatut(pret.getId(), Prolongement.StatutProlongement.APPROUVE);
        if (!prolongementsApprouves.isEmpty()) {
            LocalDate derniereDateProlongement = prolongementsApprouves.stream()
                    .map(Prolongement::getNouvelleDateRetourPrevue)
                    .max(LocalDate::compareTo)
                    .orElse(pret.getDateRetourPrevue());
            if (!pret.getDateRetourPrevue().equals(derniereDateProlongement)) {
                logger.warn("Incohérence détectée : date_retour_prevue du prêt ({}) ne correspond pas à la dernière date de prolongement approuvée ({}). Mise à jour du prêt.",
                        pret.getDateRetourPrevue(), derniereDateProlongement);
                pret.setDateRetourPrevue(derniereDateProlongement);
                pretRepository.save(pret);
            }
        }

        // Mise à jour de la date de retour réelle
        pret.setDateRetourReelle(dateRetourReelle);
        pretRepository.save(pret);
        logger.info("Date retour réelle mise à jour pour prêt id={}: {}", pret.getId(), dateRetourReelle);

        if (!prolongementsApprouves.isEmpty()) {
            logger.info("Le prêt id={} a été prolongé {} fois, dernière date de retour prévue: {}", 
                pret.getId(), prolongementsApprouves.size(), pret.getDateRetourPrevue());
        }

        // Gestion des pénalités si retard
        LocalDate dateFinPenalite = null;
        LocalDate dateLimiteRetour = pret.getDateRetourPrevue();
        boolean isDateRetourPrevueFerier = jourFerierRepository.existsByDateFerier(dateLimiteRetour);
        logger.info("Date retour prévue: {}, isDateRetourPrevueFerier: {}", dateLimiteRetour, isDateRetourPrevueFerier);
        if (isDateRetourPrevueFerier) {
            dateLimiteRetour = dateLimiteRetour.plusDays(1); // Prolonger d'un jour si date_retour_prevue est fériée
            logger.info("Date limite prolongée au lendemain: {}", dateLimiteRetour);
        }

        logger.info("Date retour réelle: {}, Date limite retour: {}", dateRetourReelle, dateLimiteRetour);
        if (dateRetourReelle.isAfter(dateLimiteRetour)) {
            logger.info("Pénalité appliquée: retour en retard");
            Penalite penalite = new Penalite();
            penalite.setAdherant(adherant);
            penalite.setPret(pret);
            penalite.setTypePenalite(Penalite.TypePenalite.RETARD);
            penalite.setDateDebutPenalite(dateRetourReelle);
            penalite.setNombreJours(NOMBRE_JOURS_PENALITE);
            penalite.setDateFinPenalite(dateRetourReelle.plusDays(NOMBRE_JOURS_PENALITE));
            penaliteRepository.save(penalite);
            logger.info("Pénalité enregistrée: id={}, dateFinPenalite={}", penalite.getId(), penalite.getDateFinPenalite());
            dateFinPenalite = penalite.getDateFinPenalite();
        } else {
            logger.info("Aucune pénalité: retour dans les délais");
        }

        // Mise à jour du quota_restant si prêt "à emporter"
        if (pret.getTypePret() == Pret.TypePret.A_EMPORTER) {
            int ancienQuota = adherant.getQuotaRestant();
            int nouveauQuota = ancienQuota + 1;
            int quotaMax = adherant.getTypeAdherant().getQuotaEmprunts();
            if (nouveauQuota > quotaMax) {
                logger.warn("Quota d'emprunts limité à {} pour adhérant id={}", quotaMax, idAdherant);
                nouveauQuota = quotaMax;
            }
            adherant.setQuotaRestant(nouveauQuota);
            adherantRepository.save(adherant);
            logger.info("Quota d'emprunts incrémenté pour adhérant id={} : {} -> {}", idAdherant, ancienQuota, nouveauQuota);
        }

        // Mise à jour du quota_restant_prolongement si le prêt a été prolongé
        if (!prolongementsApprouves.isEmpty()) {
            int ancienQuotaProlongement = adherant.getQuotaRestantProlongement();
            int nouveauQuotaProlongement = ancienQuotaProlongement + prolongementsApprouves.size();
            int quotaMaxProlongement = adherant.getTypeAdherant().getQuotaProlongements();
            if (nouveauQuotaProlongement > quotaMaxProlongement) {
                logger.warn("Quota de prolongements limité à {} pour adhérant id={}", quotaMaxProlongement, idAdherant);
                nouveauQuotaProlongement = quotaMaxProlongement;
            }
            adherant.setQuotaRestantProlongement(nouveauQuotaProlongement);
            adherantRepository.save(adherant);
            logger.info("Quota de prolongements incrémenté pour adhérant id={} : {} -> {}", idAdherant, ancienQuotaProlongement, nouveauQuotaProlongement);
        }

        // Mise à jour du statut de l'exemplaire
        boolean hasHonoredReservation = reservationRepository.hasHonoredReservation(idExemplaire);
        exemplaire.setStatut(hasHonoredReservation ? Exemplaire.StatutExemplaire.RESERVE : Exemplaire.StatutExemplaire.DISPONIBLE);
        exemplaireRepository.save(exemplaire);
        logger.info("Statut exemplaire mis à jour pour id={}: {}", exemplaire.getId(), exemplaire.getStatut());

        return dateFinPenalite; // Retourne la date de fin de pénalité si applicable, sinon null
    }
}