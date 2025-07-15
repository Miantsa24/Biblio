package com.example.biblio.controller;

import com.example.biblio.model.Adherant;
import com.example.biblio.service.AdminService;
import com.example.biblio.service.AdherantLoginService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;

@Controller
public class LoginController {

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private AdminService adminService;

    @Autowired
    private AdherantLoginService adherantLoginService;

    @GetMapping("/")
    public String showChoixLoginPage(Model model) {
        return "choix-login";
    }

    @GetMapping("/admin/login")
    public String showAdminLoginPage(Model model) {
        return "login";
    }

    @PostMapping("/admin/login")
    public String adminLogin(@RequestParam String email, @RequestParam String password, Model model, HttpSession session) {
        try {
            if (adminService.authenticate(email, password)) {
                session.setAttribute("adminLoggedIn", true);
                logger.info("Connexion réussie pour l'email admin : {}", email);
                return "redirect:/index";
            } else {
                logger.warn("Échec de la connexion pour l'email admin : {}", email);
                model.addAttribute("errorMessage", "Email ou mot de passe incorrect");
                return "login";
            }
        } catch (Exception e) {
            logger.error("Erreur lors de la tentative de connexion pour l'email admin : {}", email, e);
            model.addAttribute("errorMessage", "Une erreur s'est produite. Veuillez réessayer.");
            return "login";
        }
    }

    @GetMapping("/adherant/login")
    public String showAdherantLoginPage(Model model) {
        return "adherant-login";
    }

    @PostMapping("/adherant/login")
    public String adherantLogin(@RequestParam String email, @RequestParam String password, Model model, HttpSession session) {
        try {
            Adherant adherant = adherantLoginService.authenticate(email, password);
            session.setAttribute("adherant", adherant);
            session.setAttribute("adherantLoggedIn", true);
            session.setAttribute("adherantId", adherant.getIdAdherant());
            session.setAttribute("adherantNom", adherant.getPrenom() + " " + adherant.getNom());
            logger.info("Connexion réussie pour l'adhérant : {}", email);
            return "redirect:/adherant/index";
        } catch (IllegalArgumentException e) {
            logger.warn("Échec de la connexion pour l'adhérant : {}", email);
            model.addAttribute("errorMessage", e.getMessage());
            return "adherant-login";
        } catch (IllegalStateException e) {
            logger.warn("Échec de la connexion pour l'adhérant : {}", email);
            model.addAttribute("errorMessage", e.getMessage());
            return "adherant-login";
        } catch (Exception e) {
            logger.error("Erreur lors de la tentative de connexion pour l'adhérant : {}", email, e);
            model.addAttribute("errorMessage", "Une erreur s'est produite. Veuillez réessayer.");
            return "adherant-login";
        }
    }

    @GetMapping("/adherant/logout")
    public String adherantLogout(HttpSession session) {
        session.invalidate();
        logger.info("Déconnexion réussie pour adhérant");
        return "redirect:/";
    }

    @GetMapping("/logout")
    public String adminLogout(HttpSession session) {
        session.invalidate();
        logger.info("Déconnexion réussie pour admin");
        return "redirect:/";
    }
}