package com.example.biblio.controller;

import com.example.biblio.model.Adherant;
import com.example.biblio.model.Exemplaire;
import com.example.biblio.model.Livre;
import com.example.biblio.model.Reservation;
import com.example.biblio.model.TypeAdherant;
import com.example.biblio.repository.AdherantRepository;
import com.example.biblio.repository.ReservationRepository;
import com.example.biblio.repository.TypeAdherantRepository;
import com.example.biblio.repository.LivreRepository;
import com.example.biblio.service.AdherantService;
import com.example.biblio.service.ReservationService;
import com.example.biblio.repository.ExemplaireRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;

import jakarta.servlet.http.HttpSession;


@Controller
public class AdherantController {

    private static final Logger logger = LoggerFactory.getLogger(AdherantController.class);

    @Autowired
    private AdherantService adherantService;

    @Autowired
    private TypeAdherantRepository typeAdherantRepository;

    @Autowired
    private AdherantRepository adherantRepository;

    @Autowired
    private ExemplaireRepository exemplaireRepository;
    
    @Autowired
    private LivreRepository livreRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private ReservationService reservationService;
    
    @GetMapping("/ajouter")
    public String afficherFormulaireAjout(Model model, @RequestParam(value = "success", required = false) String success) {
        try {
            List<TypeAdherant> types = typeAdherantRepository.findAll();
            model.addAttribute("typesAdherant", types);
        } catch (Exception e) {
            logger.error("Erreur lors de la récupération des TypeAdherant : {}", e.getMessage(), e);
            model.addAttribute("errorMessage", "Erreur lors du chargement des types d'adhérants.");
            model.addAttribute("typesAdherant", Collections.emptyList());
        }

        String formattedDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        model.addAttribute("now", formattedDate);
        if ("true".equals(success)) {
            model.addAttribute("successMessage", "Adhérant ajouté avec succès !");
        }
        return "ajouter-adherant";
    }

    @PostMapping("/ajouter")
    public String ajouterAdherant(@RequestParam String nom,
                                  @RequestParam String prenom,
                                  @RequestParam String dateNaissance,
                                  @RequestParam String email,
                                  @RequestParam String nomTypeAdherant,
                                  @RequestParam String motDePasse,
                                  @RequestParam String dateDebutAbonnement,
                                  Model model) {
        // Validation de base des paramètres
        if (nom == null || nom.trim().isEmpty() || prenom == null || prenom.trim().isEmpty() ||
            email == null || email.trim().isEmpty() || motDePasse == null || motDePasse.trim().isEmpty() ||
            dateNaissance == null || dateNaissance.trim().isEmpty() || dateDebutAbonnement == null || dateDebutAbonnement.trim().isEmpty()) {
            logger.warn("Champs obligatoires manquants lors de l'ajout de l'adhérant : {}", email);
            model.addAttribute("errorMessage", "Tous les champs sont obligatoires.");
            try {
                model.addAttribute("typesAdherant", typeAdherantRepository.findAll());
            } catch (Exception ex) {
                logger.error("Erreur lors de la récupération des TypeAdherant (POST) : {}", ex.getMessage(), ex);
                model.addAttribute("typesAdherant", Collections.emptyList());
            }
            model.addAttribute("nom", nom);
            model.addAttribute("prenom", prenom);
            model.addAttribute("email", email);
            model.addAttribute("dateNaissance", dateNaissance);
            model.addAttribute("nomTypeAdherant", nomTypeAdherant);
            model.addAttribute("dateDebutAbonnement", dateDebutAbonnement);
            model.addAttribute("now", LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            return "ajouter-adherant";
        }

        try {
            adherantService.ajouterAdherant(
                nom.trim(), prenom.trim(), LocalDate.parse(dateNaissance), email.trim(), nomTypeAdherant,
                motDePasse.trim(), LocalDate.parse(dateDebutAbonnement));
            logger.info("Adhérant ajouté avec succès : {}", email);
            return "redirect:/ajouter?success=true";
        } catch (IllegalArgumentException e) {
            logger.error("Erreur lors de l'ajout de l'adhérant : {}", e.getMessage(), e);
            model.addAttribute("errorMessage", e.getMessage());
            try {
                model.addAttribute("typesAdherant", typeAdherantRepository.findAll());
            } catch (Exception ex) {
                logger.error("Erreur lors de la récupération des TypeAdherant (POST) : {}", ex.getMessage(), ex);
                model.addAttribute("typesAdherant", Collections.emptyList());
                model.addAttribute("errorMessage", "Erreur lors du chargement des types d'adhérants : " + ex.getMessage());
            }
            model.addAttribute("nom", nom);
            model.addAttribute("prenom", prenom);
            model.addAttribute("email", email);
            model.addAttribute("dateNaissance", dateNaissance);
            model.addAttribute("nomTypeAdherant", nomTypeAdherant);
            model.addAttribute("dateDebutAbonnement", dateDebutAbonnement);
            model.addAttribute("now", LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            return "ajouter-adherant";
        } catch (Exception e) {
            logger.error("Erreur inattendue lors de l'ajout de l'adhérant : {}", e.getMessage(), e);
            model.addAttribute("errorMessage", "Une erreur inattendue s'est produite : " + e.getMessage());
            model.addAttribute("typesAdherant", Collections.emptyList());
            model.addAttribute("nom", nom);
            model.addAttribute("prenom", prenom);
            model.addAttribute("email", email);
            model.addAttribute("dateNaissance", dateNaissance);
            model.addAttribute("nomTypeAdherant", nomTypeAdherant);
            model.addAttribute("dateDebutAbonnement", dateDebutAbonnement);
            model.addAttribute("now", LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            return "ajouter-adherant";
        }
    }

    @GetMapping("/adherants")
    public String listAdherants(Model model) {
        try {
            LocalDate currentDate = LocalDate.now();
            logger.info("Récupération des adhérants avec abonnement valide au : {}", currentDate);
            List<Adherant> adherants = adherantRepository.findAllWithValidAbonnement(currentDate);
            for (Adherant adherant : adherants) {
                adherant.getAbonnements().size();
                logger.debug("Adhérant ID: {}, Nom: {}, Abonnements: {}", 
                             adherant.getIdAdherant(), adherant.getNom() + " " + adherant.getPrenom(), adherant.getAbonnements());
            }
            logger.info("Adhérants avec abonnement valide trouvés : {}", adherants.size());
            model.addAttribute("adherants", adherants);
            model.addAttribute("currentDate", currentDate);
        } catch (Exception e) {
            logger.error("Erreur lors de la récupération des Adhérants : {}", e.getMessage(), e);
            model.addAttribute("errorMessage", "Erreur lors du chargement des adhérants.");
            model.addAttribute("adherants", Collections.emptyList());
        }
        return "liste-adherants";
    }

    @GetMapping("/renouveler")
    public String afficherFormulaireRenouvellement(Model model, @RequestParam(value = "success", required = false) String success) {
        String formattedDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        model.addAttribute("now", formattedDate);
        if ("true".equals(success)) {
            model.addAttribute("successMessage", "Abonnement renouvelé avec succès !");
        }
        return "renouveler-abonnement";
    }

    @PostMapping("/renouveler")
    public String renouvelerAbonnement(@RequestParam Integer idAdherant,
                                      @RequestParam String dateDebutAbonnement,
                                      Model model) {
        try {
            adherantService.renouvelerAbonnement(idAdherant, LocalDate.parse(dateDebutAbonnement));
            return "redirect:/renouveler?success=true";
        } catch (IllegalArgumentException e) {
            logger.error("Erreur lors du renouvellement de l'abonnement : {}", e.getMessage(), e);
            model.addAttribute("errorMessage", e.getMessage());
            String formattedDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            model.addAttribute("now", formattedDate);
            return "renouveler-abonnement";
        }
    }

    @GetMapping("/adherant/index")
    public String showAdherantIndex(Model model, HttpSession session) {
        if (session.getAttribute("adherantLoggedIn") == null) {
            logger.warn("Tentative d'accès à /adherant/index sans connexion");
            return "redirect:/adherant/login";
        }
        logger.info("Affichage de la page d'accueil adhérant pour : {}", session.getAttribute("adherantNom"));
        return "adherant-index";
    }

        @GetMapping("/adherant/livres")
    public String listLivres(Model model, HttpSession session) {
        if (session.getAttribute("adherantLoggedIn") == null) {
            logger.warn("Tentative d'accès à /adherant/livres sans connexion");
            return "redirect:/adherant/login";
        }
        try {
            List<Livre> livres = livreRepository.findAll();
            for (Livre livre : livres) {
                livre.getExemplaires().size(); // Initialize lazy-loaded exemplaires
                logger.debug("Livre ID: {}, Titre: {}, Exemplaires: {}", 
                             livre.getId(), livre.getTitre(), livre.getExemplaires());
            }
            logger.info("Livres trouvés : {}", livres.size());
            model.addAttribute("livres", livres);
        } catch (Exception e) {
            logger.error("Erreur lors de la récupération des livres : {}", e.getMessage(), e);
            model.addAttribute("errorMessage", "Erreur lors du chargement des livres.");
            model.addAttribute("livres", Collections.emptyList());
        }
        return "livres";
    }

    @GetMapping("/adherant/reservations")
    public String showReservationForm(Model model, @RequestParam(required = false) Integer idExemplaire) {
        model.addAttribute("idExemplaire", idExemplaire);
        return "reservations";
    }

    @PostMapping("/adherant/reservations")
    public String submitReservation(@RequestParam Integer idAdherant, @RequestParam Integer idExemplaire, 
                                   @RequestParam String dateReservation, Model model) {
        try {
            LocalDate reservationDate = LocalDate.parse(dateReservation);
            reservationService.reserverLivre(idAdherant, idExemplaire, reservationDate);
            model.addAttribute("successMessage", "Réservation envoyée avec succès. En attente de validation par l'admin.");
        } catch (IllegalArgumentException | IllegalStateException e) {
            logger.error("Erreur lors de la création de la réservation : {}", e.getMessage());
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("idExemplaire", idExemplaire);
        }
        return "reservations";
    }

    @GetMapping("/adherant/mes-reservations")
public String listUserReservations(Model model, HttpSession session) {
    Adherant adherant = (Adherant) session.getAttribute("adherant");
    if (adherant == null) {
        logger.warn("Tentative d'accès à /adherant/mes-reservations sans connexion");
        return "redirect:/adherant/login";
    }
    Integer idAdherant = adherant.getIdAdherant();
    try {
        // Récupérer toutes les réservations pour l'adhérant, sans filtrer par statut
        List<Reservation> reservations = reservationRepository.findByIdAdherant(idAdherant);
        // Initialiser le champ transitoire exemplaire pour chaque réservation
        for (Reservation reservation : reservations) {
            Exemplaire exemplaire = exemplaireRepository.findById(reservation.getIdExemplaire()).orElse(null);
            reservation.setExemplaire(exemplaire);
        }
        model.addAttribute("reservations", reservations);
        return "mes-reservations";
    } catch (Exception e) {
        logger.error("Erreur lors de la récupération des réservations pour l'adhérant ID {} : {}", idAdherant, e.getMessage(), e);
        model.addAttribute("errorMessage", "Erreur lors du chargement des réservations. Veuillez réessayer.");
        model.addAttribute("reservations", Collections.emptyList());
        return "mes-reservations";
    }
}
}