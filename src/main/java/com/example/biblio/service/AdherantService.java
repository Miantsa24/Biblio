package com.example.biblio.service;

import com.example.biblio.model.Adherant;
import com.example.biblio.model.Abonnement;
import com.example.biblio.model.TypeAdherant;
import com.example.biblio.repository.AdherantRepository;
import com.example.biblio.repository.AbonnementRepository;
import com.example.biblio.repository.TypeAdherantRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class AdherantService {

    private static final Logger logger = LoggerFactory.getLogger(AdherantService.class);

    @Autowired
    private AdherantRepository adherantRepository;

    @Autowired
    private TypeAdherantRepository typeAdherantRepository;

    @Autowired
    private AbonnementRepository abonnementRepository;

    public Adherant ajouterAdherant(String nom, String prenom, LocalDate dateNaissance, String email,
                                    String nomTypeAdherant, String motDePasse,
                                    LocalDate dateDebutAbonnement) {
        logger.debug("Ajout d'un adhérant : {} {} (email: {})", nom, prenom, email);

        // Vérification des champs obligatoires
        if (nom == null || nom.isEmpty() || prenom == null || prenom.isEmpty() ||
            dateNaissance == null || motDePasse == null || motDePasse.isEmpty()) {
            logger.error("Champs obligatoires manquants pour l'adhérant : {}", email);
            throw new IllegalArgumentException("Nom, prénom, date de naissance, et mot de passe sont obligatoires");
        }

        // Vérification de l'unicité de l'email
        if (adherantRepository.existsByEmail(email)) {
            logger.error("Email déjà utilisé : {}", email);
            throw new IllegalArgumentException("Email déjà utilisé");
        }

        // Vérification de l'unicité du mot de passe
        if (adherantRepository.existsByMotDePasse(motDePasse)) {
            logger.error("Mot de passe déjà utilisé : {}", motDePasse);
            throw new IllegalArgumentException("Mot de passe déjà utilisé");
        }

        // Vérification du type d'adhérant
        TypeAdherant typeAdherant = typeAdherantRepository.findByNomType(nomTypeAdherant)
                .orElseThrow(() -> {
                    logger.error("Type d'adhérant invalide : {}", nomTypeAdherant);
                    return new IllegalArgumentException("Type d'adhérant invalide : " + nomTypeAdherant);
                });

        // Vérification de la date de début d'abonnement
        if (dateDebutAbonnement == null) {
            logger.error("Date de début d'abonnement manquante pour l'adhérant : {}", email);
            throw new IllegalArgumentException("La date de début d'abonnement est obligatoire");
        }

        // Calcul de la date de fin d'abonnement (365 jours après la date de début)
        LocalDate dateFinAbonnement = dateDebutAbonnement.plusDays(365);
        logger.debug("Abonnement créé de {} à {}", dateDebutAbonnement, dateFinAbonnement);

        // Création de l'adhérant avec mot de passe en texte clair
        Adherant adherant = new Adherant(nom, prenom, dateNaissance, email, typeAdherant, motDePasse);
        adherant.setQuotaRestant(typeAdherant.getQuotaEmprunts());
        adherant.setQuotaRestantReservation(typeAdherant.getQuotaReservations());
        adherant.setQuotaRestantProlongement(typeAdherant.getQuotaProlongements());

        // Sauvegarde de l'adhérant
        adherantRepository.save(adherant);
        logger.info("Adhérant sauvegardé : {} (ID: {})", email, adherant.getIdAdherant());

        // Création de l'abonnement
        Abonnement abonnement = new Abonnement(adherant, dateDebutAbonnement, dateFinAbonnement);
        abonnementRepository.save(abonnement);
        logger.info("Abonnement créé pour l'adhérant : {}", email);

        return adherant;
    }

    public void renouvelerAbonnement(Integer idAdherant, LocalDate nouvelleDateDebut) {
        logger.debug("Renouvellement d'abonnement pour l'adhérant ID: {}", idAdherant);
        // Vérifier si l'adhérant existe
        Adherant adherant = adherantRepository.findById(idAdherant)
                .orElseThrow(() -> {
                    logger.error("Adhérant non trouvé : ID {}", idAdherant);
                    return new IllegalArgumentException("Adhérant non trouvé");
                });

        // Vérifier les pénalités actives
        if (abonnementRepository.hasActivePenalite(idAdherant, LocalDate.now())) {
            logger.error("Pénalité active pour l'adhérant : ID {}", idAdherant);
            throw new IllegalArgumentException("L'adhérant a une pénalité active");
        }

        // Trouver l'abonnement actuel le plus récent
        Optional<Abonnement> currentAbonnementOpt = abonnementRepository.findByAdherantIdAndDateFinAfter(idAdherant, LocalDate.now());
        if (currentAbonnementOpt.isPresent()) {
            LocalDate currentDateFin = currentAbonnementOpt.get().getDateFin();
            // Vérifier que la nouvelle date de début est postérieure à la date de fin actuelle
            if (nouvelleDateDebut.isBefore(currentDateFin) || nouvelleDateDebut.isEqual(currentDateFin)) {
                logger.error("Date de début invalide pour renouvellement : {} (doit être postérieure à {})", nouvelleDateDebut, currentDateFin);
                throw new IllegalArgumentException("La nouvelle date de début doit être postérieure à la date de fin actuelle (" + currentDateFin + ")");
            }
        }

        // Calculer la nouvelle date de fin (365 jours après la nouvelle date de début)
        LocalDate nouvelleDateFin = nouvelleDateDebut.plusDays(365);

        // Créer un nouvel abonnement
        Abonnement nouvelAbonnement = new Abonnement(adherant, nouvelleDateDebut, nouvelleDateFin);
        abonnementRepository.save(nouvelAbonnement);
        logger.info("Abonnement renouvelé pour l'adhérant ID: {} (de {} à {})", idAdherant, nouvelleDateDebut, nouvelleDateFin);
    }
}