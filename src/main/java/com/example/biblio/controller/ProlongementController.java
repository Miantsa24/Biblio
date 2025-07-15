package com.example.biblio.controller;

import com.example.biblio.model.Prolongement;
import com.example.biblio.repository.AdherantRepository;
import com.example.biblio.repository.ExemplaireRepository;
import com.example.biblio.repository.LivreRepository;
import com.example.biblio.service.ProlongementService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/prolongements")
public class ProlongementController {

    private static final Logger logger = LoggerFactory.getLogger(ProlongementController.class);

    @Autowired
    private ProlongementService prolongementService;

    @Autowired
    private AdherantRepository adherantRepository;

    @Autowired
    private ExemplaireRepository exemplaireRepository;

    @Autowired
    private LivreRepository livreRepository;

    @GetMapping("/liste")
    public String listProlongements(Model model) {
        List<Prolongement> prolongementsEnAttente = prolongementService.getAllPendingProlongements();
        model.addAttribute("prolongementsEnAttente", prolongementsEnAttente);
        model.addAttribute("adherantRepository", adherantRepository);
        model.addAttribute("exemplaireRepository", exemplaireRepository);
        model.addAttribute("livreRepository", livreRepository);
        return "liste-prolongements";
    }

    @PostMapping("/approuver")
    public String approuverProlongement(@RequestParam Integer idProlongement, Model model) {
        try {
            prolongementService.approuverProlongement(idProlongement);
            return "redirect:/prolongements/liste?success=approuver";
        } catch (IllegalArgumentException | IllegalStateException e) {
            logger.error("Erreur lors de l'approbation du prolongement : {}", e.getMessage());
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("prolongementsEnAttente", prolongementService.getAllPendingProlongements());
            model.addAttribute("adherantRepository", adherantRepository);
            model.addAttribute("exemplaireRepository", exemplaireRepository);
            model.addAttribute("livreRepository", livreRepository);
            return "liste-prolongements";
        }
    }

    @PostMapping("/refuser")
    public String refuserProlongement(@RequestParam Integer idProlongement, Model model) {
        try {
            prolongementService.refuserProlongement(idProlongement);
            return "redirect:/prolongements/liste?success=refuser";
        } catch (IllegalArgumentException | IllegalStateException e) {
            logger.error("Erreur lors du refus du prolongement : {}", e.getMessage());
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("prolongementsEnAttente", prolongementService.getAllPendingProlongements());
            model.addAttribute("adherantRepository", adherantRepository);
            model.addAttribute("exemplaireRepository", exemplaireRepository);
            model.addAttribute("livreRepository", livreRepository);
            return "liste-prolongements";
        }
    }
}