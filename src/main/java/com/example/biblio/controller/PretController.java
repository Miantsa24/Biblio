
package com.example.biblio.controller;

import com.example.biblio.model.Livre;
import com.example.biblio.model.Pret;
import com.example.biblio.repository.LivreRepository;
import com.example.biblio.repository.PretRepository;
import com.example.biblio.service.PretService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/prets")
public class PretController {

    @Autowired
    private PretService pretService;

    @Autowired
    private LivreRepository livreRepository;

    @Autowired
    private PretRepository pretRepository;

    @GetMapping
    public String afficherFormulairePret(Model model, @RequestParam(value = "success", required = false) String success) {
        List<Livre> livres = livreRepository.findAll();
        model.addAttribute("livres", livres);
        if ("true".equals(success)) {
            model.addAttribute("successMessage", "Livre prêté avec succès !");
        }
        return "preterLivre";
    }

    @PostMapping
    public String preterLivre(@RequestParam Integer idAdherant,
                              @RequestParam Integer idLivre,
                              @RequestParam String typePret,
                              @RequestParam String datePret,
                              Model model) {
        try {
            pretService.preterLivre(idAdherant, idLivre, typePret, LocalDate.parse(datePret));
            return "redirect:/prets?success=true";
        } catch (IllegalArgumentException | IllegalStateException e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("livres", livreRepository.findAll());
            return "preterLivre";
        }
    }

    @GetMapping("/liste")
    public String listPrets(Model model) {
        List<Pret> pretsEnCours = pretRepository.findAllByDateRetourReelleIsNull();
        model.addAttribute("pretsEnCours", pretsEnCours);
        return "liste-prets";
    }
}
