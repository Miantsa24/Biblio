
package com.example.biblio.controller;

import com.example.biblio.service.RendreLivreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Controller
@RequestMapping("/retours")
public class RendreLivreController {

    private static final Logger logger = LoggerFactory.getLogger(RendreLivreController.class);

    @Autowired
    private RendreLivreService rendreLivreService;

    @GetMapping
    public String afficherFormulaireRetour(Model model, HttpSession session) {
        String penaltyMessage = (String) session.getAttribute("penaltyMessage");
        if (penaltyMessage != null) {
            model.addAttribute("penaltyMessage", penaltyMessage);
            session.removeAttribute("penaltyMessage");
        }
        return "rendreLivre";
    }

    @PostMapping
    public String rendreLivre(@RequestParam Integer idAdherant,
                              @RequestParam Integer idExemplaire,
                              @RequestParam String dateRetourReelle,
                              Model model,
                              HttpSession session) {
        try {
            LocalDate parsedDateRetourReelle;
            try {
                parsedDateRetourReelle = LocalDate.parse(dateRetourReelle);
                logger.info("Date retour réelle reçue (YYYY-MM-DD): {}, parsée: {}", dateRetourReelle, parsedDateRetourReelle);
            } catch (DateTimeParseException e) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                parsedDateRetourReelle = LocalDate.parse(dateRetourReelle, formatter);
                logger.info("Date retour réelle reçue (DD/MM/YYYY): {}, parsée: {}", dateRetourReelle, parsedDateRetourReelle);
            }

            LocalDate dateFinPenalite = rendreLivreService.rendreLivre(idAdherant, idExemplaire, parsedDateRetourReelle);
            if (dateFinPenalite != null) {
                session.setAttribute("penaltyMessage", "Livre rendu avec succès, mais vous êtes pénalisé jusqu'au " + dateFinPenalite);
            } else {
                session.setAttribute("penaltyMessage", "Livre rendu avec succès !");
            }
            return "redirect:/retours?success=true";
        } catch (DateTimeParseException e) {
            logger.error("Erreur de parsing de la date: {}", dateRetourReelle, e);
            model.addAttribute("errorMessage", "Format de date invalide. Utilisez YYYY-MM-DD ou DD/MM/YYYY.");
            return "rendreLivre";
        } catch (IllegalArgumentException | IllegalStateException e) {
            logger.error("Erreur lors du retour: {}", e.getMessage());
            model.addAttribute("errorMessage", e.getMessage());
            return "rendreLivre";
        } catch (Exception e) {
            logger.error("Erreur inattendue lors du retour du livre: {}", e.getMessage(), e);
            model.addAttribute("errorMessage", "Une erreur inattendue s'est produite. Veuillez réessayer ou contacter l'administrateur.");
            return "rendreLivre";
        }
    }
}
