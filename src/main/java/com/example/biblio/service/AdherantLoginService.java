package com.example.biblio.service;

import com.example.biblio.model.Adherant;
import com.example.biblio.repository.AdherantRepository;
import com.example.biblio.repository.AbonnementRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class AdherantLoginService {

    private static final Logger logger = LoggerFactory.getLogger(AdherantLoginService.class);

    @Autowired
    private AdherantRepository adherantRepository;

    @Autowired
    private AbonnementRepository abonnementRepository;

    public Adherant authenticate(String email, String password) {
        logger.debug("Tentative d'authentification pour l'adhérant avec l'email : {}", email);
        Optional<Adherant> adherantOpt = adherantRepository.findByEmail(email);
        if (adherantOpt.isEmpty()) {
            logger.warn("Aucun adhérant trouvé pour l'email : {}", email);
            throw new IllegalArgumentException("Email ou mot de passe incorrect");
        }

        Adherant adherant = adherantOpt.get();
        logger.debug("Adhérant trouvé : {} (ID: {})", adherant.getEmail(), adherant.getIdAdherant());

        // Vérifier le mot de passe en texte clair
        if (!password.equals(adherant.getMotDePasse())) {
            logger.warn("Échec de la vérification du mot de passe pour l'adhérant : {}", email);
            throw new IllegalArgumentException("Email ou mot de passe incorrect");
        }
        logger.debug("Mot de passe vérifié avec succès pour l'adhérant : {}", email);

        // Vérifier la validité de l'abonnement
        if (!adherant.hasValidAbonnement(LocalDate.now())) {
            logger.warn("Abonnement non valide pour l'adhérant : {}", email);
            throw new IllegalStateException("Abonnement non valide");
        }

        // Vérifier les sanctions actives
        if (abonnementRepository.hasActivePenalite(adherant.getIdAdherant(), LocalDate.now())) {
            logger.warn("Compte sanctionné pour l'adhérant : {}", email);
            throw new IllegalStateException("Compte sanctionné");
        }

        logger.info("Authentification réussie pour l'adhérant : {}", email);
        return adherant;
    }
}