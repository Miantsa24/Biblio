
package com.example.biblio.controller;

import com.example.biblio.model.JourFerier;
import com.example.biblio.repository.JourFerierRepository;
import com.example.biblio.service.JourFerierService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
@RequestMapping("/jours-feries")
public class JourFerierController {

    private static final Logger logger = LoggerFactory.getLogger(JourFerierController.class);

    @Autowired
    private JourFerierService jourFerierService;

    @Autowired
    private JourFerierRepository jourFerierRepository;

    @GetMapping
    public String afficherFormulaireEtListe(Model model, @RequestParam(value = "success", required = false) String success) {
        // Charger la liste des jours fériés
        List<JourFerier> joursFeries = jourFerierRepository.findAll();
        logger.info("Jours fériés récupérés : {}", joursFeries);
        model.addAttribute("joursFeries", joursFeries);

        // Ajouter la date actuelle pour le formulaire
        String formattedDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        model.addAttribute("today", formattedDate);

        // Message de succès si applicable
        if ("true".equals(success)) {
            model.addAttribute("successMessage", "Jour férié ajouté avec succès !");
        }

        return "gerer-jours-feries";
    }

    @PostMapping
    public String ajouterJourFerier(@RequestParam String dateFerier,
                                    @RequestParam(required = false) String description,
                                    Model model) {
        try {
            jourFerierService.ajouterJourFerier(LocalDate.parse(dateFerier), description);
            logger.info("Jour férié ajouté : date={}, description={}", dateFerier, description);
            return "redirect:/jours-feries?success=true";
        } catch (IllegalArgumentException e) {
            logger.error("Erreur lors de l'ajout du jour férié : {}", e.getMessage());
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("joursFeries", jourFerierRepository.findAll());
            String formattedDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            model.addAttribute("today", formattedDate);
            return "gerer-jours-feries";
        }
    }
}