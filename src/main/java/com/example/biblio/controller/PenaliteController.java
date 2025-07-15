package com.example.biblio.controller;

import com.example.biblio.model.Penalite;
import com.example.biblio.service.PenaliteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/penalites")
public class PenaliteController {

    private static final Logger logger = LoggerFactory.getLogger(PenaliteController.class);

    @Autowired
    private PenaliteService penaliteService;

    @GetMapping
    public String afficherPenalitesEnCours(Model model) {
        List<Penalite> penalites = penaliteService.getPenalitesEnCours();
        logger.info("Pénalités en cours récupérées : {}", penalites);
        model.addAttribute("penalites", penalites);
        return "penalitesEnCours";
    }
}