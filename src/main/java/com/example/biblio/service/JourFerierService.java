
package com.example.biblio.service;

import com.example.biblio.model.JourFerier;
import com.example.biblio.repository.JourFerierRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
public class JourFerierService {

    @Autowired
    private JourFerierRepository jourFerierRepository;

    @Transactional
    public void ajouterJourFerier(LocalDate dateFerier, String description) {
        // Vérification de la date : non nulle
        if (dateFerier == null) {
            throw new IllegalArgumentException("La date du jour férié est obligatoire");
        }

        // Vérification de l'unicité
        if (jourFerierRepository.existsByDateFerier(dateFerier)) {
            throw new IllegalArgumentException("Cette date est déjà enregistrée comme jour férié");
        }

        // Création et enregistrement du jour férié
        JourFerier jourFerier = new JourFerier();
        jourFerier.setDateFerier(dateFerier);
        jourFerier.setDescription(description != null && !description.isEmpty() ? description : null);
        jourFerierRepository.save(jourFerier);
    }
}