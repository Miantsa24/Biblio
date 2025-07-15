package com.example.biblio.controller;

import com.example.biblio.model.Abonnement;
import com.example.biblio.model.Adherant;
import com.example.biblio.model.Exemplaire;
import com.example.biblio.model.Livre;
import com.example.biblio.model.Pret;
import com.example.biblio.model.Reservation;
import com.example.biblio.model.TypeAdherant;
import com.example.biblio.repository.AdherantRepository;
import com.example.biblio.repository.AbonnementRepository;
import com.example.biblio.repository.ReservationRepository;
import com.example.biblio.repository.TypeAdherantRepository;
import com.example.biblio.repository.LivreRepository;
import com.example.biblio.repository.PretRepository;
import com.example.biblio.repository.ProlongementRepository;
import com.example.biblio.service.AdherantService;
import com.example.biblio.service.ReservationService;
import com.example.biblio.service.ProlongementService;
import com.example.biblio.repository.ExemplaireRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
// import org.springframework.http.HttpStatus;
// import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
// import java.util.Map;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
// import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import jakarta.servlet.http.HttpSession;

import java.util.Optional;
import java.util.Set;


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
    private AbonnementRepository abonnementRepository;

    @Autowired
    private ExemplaireRepository exemplaireRepository;

    @Autowired
    private LivreRepository livreRepository;

    @Autowired
    private PretRepository pretRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private ProlongementService prolongementService;

    @Autowired
    private ProlongementRepository prolongementRepository;

    @GetMapping("/ajouter")
    public String afficherFormulaireAjout(Model model,
            @RequestParam(value = "success", required = false) String success) {
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
                dateNaissance == null || dateNaissance.trim().isEmpty() || dateDebutAbonnement == null
                || dateDebutAbonnement.trim().isEmpty()) {
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
                model.addAttribute("errorMessage",
                        "Erreur lors du chargement des types d'adhérants : " + ex.getMessage());
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
                        adherant.getIdAdherant(), adherant.getNom() + " " + adherant.getPrenom(),
                        adherant.getAbonnements());
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
    public String afficherFormulaireRenouvellement(Model model,
            @RequestParam(value = "success", required = false) String success) {
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
        try {
            // Vérifier si un adhérant est connecté
            Integer idAdherant = (Integer) session.getAttribute("adherantLoggedIn");
            if (idAdherant == null) {
                logger.warn("Tentative d'accès à /adherant/livres sans connexion.");
                return "redirect:/adherant/login";
            }

            LocalDate currentDate = LocalDate.now();
            logger.info("Récupération des livres pour l'adhérant ID {} à la date {}", idAdherant, currentDate);

            // Récupérer tous les livres
            List<Livre> livres = livreRepository.findAll();
            // Récupérer les réservations actives de l'adhérant
            List<Reservation> reservations = reservationRepository.findByIdAdherant(idAdherant);
            Set<Integer> exemplaireReservations = reservations.stream()
                    .filter(r -> r.getStatut() == Reservation.StatutReservation.EN_ATTENTE || r.getStatut() == Reservation.StatutReservation.HONOREE)
                    .map(Reservation::getIdExemplaire)
                    .collect(Collectors.toSet());
            // Récupérer les réservations honorées uniquement
            Set<Integer> exemplaireReservationsHonorees = reservations.stream()
                    .filter(r -> r.getStatut() == Reservation.StatutReservation.HONOREE)
                    .map(Reservation::getIdExemplaire)
                    .collect(Collectors.toSet());

            // Récupérer les prêts en cours de l'adhérant
            List<Pret> pretsEnCours = pretRepository.findByAdherantIdAndDateRetourReelleIsNull(idAdherant);
            Set<Integer> exemplairePrets = pretsEnCours.stream()
                    .map(pret -> pret.getExemplaire().getId())
                    .collect(Collectors.toSet());

            // Mettre à jour les statuts des exemplaires
            for (Livre livre : livres) {
                livre.getExemplaires().size(); // Initialiser la liste des exemplaires
                for (Exemplaire exemplaire : livre.getExemplaires()) {
                    // Si l'exemplaire est réservé avec une réservation honorée
                    if (exemplaire.getStatut() == Exemplaire.StatutExemplaire.RESERVE) {
                        Reservation reservation = reservationRepository.findTopPriorityReservationByExemplaireId(exemplaire.getId());
                        if (reservation != null && reservation.getStatut() == Reservation.StatutReservation.HONOREE) {
                            // Vérifier si le prêt associé est rendu
                            Pret pret = pretRepository.findByExemplaireIdAndDateRetourReelleIsNull(exemplaire.getId());
                            if (pret == null) { // Aucun prêt en cours, donc rendu
                                boolean hasOtherActiveReservations = reservationRepository.hasActiveReservation(exemplaire.getId());
                                if (!hasOtherActiveReservations) {
                                    exemplaire.setStatut(Exemplaire.StatutExemplaire.DISPONIBLE);
                                    reservation.setStatut(Reservation.StatutReservation.TERMINEE);
                                    reservationRepository.save(reservation);
                                    exemplaireRepository.save(exemplaire);
                                    logger.info("Exemplaire ID {} mis à jour à DISPONIBLE car réservation honorée et prêt rendu.", exemplaire.getId());
                                }
                            }
                        }
                    }
                }
            }

            model.addAttribute("livres", livres);
            model.addAttribute("exemplaireReservations", exemplaireReservations);
            model.addAttribute("exemplaireReservationsHonorees", exemplaireReservationsHonorees);
            model.addAttribute("exemplairePrets", exemplairePrets);
            model.addAttribute("currentDate", currentDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));

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
            model.addAttribute("successMessage",
                    "Réservation envoyée avec succès. En attente de validation par l'admin.");
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
            List<Reservation> reservations = reservationRepository.findByIdAdherant(idAdherant);
            for (Reservation reservation : reservations) {
                Exemplaire exemplaire = exemplaireRepository.findById(reservation.getIdExemplaire()).orElse(null);
                reservation.setExemplaire(exemplaire);
            }
            model.addAttribute("reservations", reservations);
            return "mes-reservations";
        } catch (Exception e) {
            logger.error("Erreur lors de la récupération des réservations pour l'adhérant ID {} : {}", idAdherant,
                    e.getMessage(), e);
            model.addAttribute("errorMessage", "Erreur lors du chargement des réservations. Veuillez réessayer.");
            model.addAttribute("reservations", Collections.emptyList());
            return "mes-reservations";
        }
    }

    @GetMapping("/adherant/mes-prets")
    public String listUserPrets(Model model, HttpSession session) {
        Adherant adherant = (Adherant) session.getAttribute("adherant");
        if (adherant == null) {
            logger.warn("Tentative d'accès à /adherant/mes-prets sans connexion");
            return "redirect:/adherant/login";
        }
        Integer idAdherant = adherant.getIdAdherant();
        try {
            List<Pret> pretsEnCours = pretRepository.findByAdherantIdAndDateRetourReelleIsNull(idAdherant);
            for (Pret pret : pretsEnCours) {
                pret.getExemplaire().getLivre(); // Initialiser les relations lazy-loaded
            }
            model.addAttribute("pretsEnCours", pretsEnCours);
            model.addAttribute("adherant", adherant);
            model.addAttribute("prolongementRepository", prolongementRepository);
            logger.info("Prêts en cours récupérés pour l'adhérant ID {} : {}", idAdherant, pretsEnCours.size());
            return "mes-prets";
        } catch (Exception e) {
            logger.error("Erreur lors de la récupération des prêts pour l'adhérant ID {} : {}", idAdherant, e.getMessage(), e);
            model.addAttribute("errorMessage", "Erreur lors du chargement des prêts. Veuillez réessayer.");
            model.addAttribute("pretsEnCours", Collections.emptyList());
            model.addAttribute("adherant", adherant);
            return "mes-prets";
        }
    }

    @PostMapping("/adherant/prolongements/demander")
    public String demanderProlongement(@RequestParam Integer idPret, HttpSession session, Model model) {
        Adherant adherant = (Adherant) session.getAttribute("adherant");
        if (adherant == null) {
            logger.warn("Tentative de demande de prolongement sans connexion");
            return "redirect:/adherant/login";
        }
        try {
            prolongementService.demanderProlongement(idPret, adherant.getIdAdherant());
            model.addAttribute("successMessage", "Demande de prolongement envoyée avec succès.");
            return "redirect:/adherant/mes-prets?success=true";
        } catch (IllegalArgumentException | IllegalStateException e) {
            logger.error("Erreur lors de la demande de prolongement : {}", e.getMessage());
            model.addAttribute("errorMessage", e.getMessage());
            List<Pret> pretsEnCours = pretRepository.findByAdherantIdAndDateRetourReelleIsNull(adherant.getIdAdherant());
            model.addAttribute("pretsEnCours", pretsEnCours);
            model.addAttribute("adherant", adherant);
            model.addAttribute("prolongementRepository", prolongementRepository);
            return "mes-prets";
        }
    }

   @GetMapping("/adherant/livres/{idLivre}")
    @ResponseBody
    public ResponseEntity<?> getLivreAndExemplaires(@PathVariable Integer idLivre) {
        try {
            // Vérifier l'existence du livre
            Livre livre = livreRepository.findById(idLivre)
                    .orElseThrow(() -> new IllegalArgumentException("Livre non trouvé"));

            // Récupérer les exemplaires du livre
            List<Exemplaire> exemplaires = exemplaireRepository.findByLivreId(idLivre);
            logger.debug("Livre ID: {}, Titre: {}, Exemplaires trouvés: {}", livre.getId(), livre.getTitre(), exemplaires.size());

            // Construire les informations du livre
            Map<String, Object> livreData = new HashMap<>();
            livreData.put("id", livre.getId());
            livreData.put("titre", livre.getTitre());
            livreData.put("auteur", livre.getAuteur());
            livreData.put("ageMinimum", livre.getAgeMinimum());
            livreData.put("isbn", livre.getIsbn());

            // Construire l'objet des exemplaires (idExemplaire -> statut)
            Map<String, String> exemplairesData = new HashMap<>();
            for (Exemplaire exemplaire : exemplaires) {
                exemplairesData.put(String.valueOf(exemplaire.getId()), exemplaire.getStatut().toString());
            }

            // Construire la réponse JSON avec "livre" en premier
            Map<String, Object> response = new LinkedHashMap<>(); // Utiliser LinkedHashMap pour garantir l'ordre
            response.put("livre", livreData);
            response.put("exemplaires", exemplairesData);

            logger.info("Livre ID {} trouvé, Exemplaires : {}", idLivre, exemplaires.size());

            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            logger.error("Erreur lors de la récupération du livre ID {} : {}", idLivre, e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.singletonMap("error", e.getMessage()));
        } catch (Exception e) {
            logger.error("Erreur inattendue lors de la récupération du livre ID {} : {}", idLivre, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("error", "Erreur lors du chargement du livre et des exemplaires."));
        }
    }


    @GetMapping("/adherants/{idAdherant}")
    @ResponseBody
    public ResponseEntity<?> getAdherantInfo(@PathVariable Integer idAdherant) {
        try {
            // Vérifier l'existence de l'adhérant
            Adherant adherant = adherantRepository.findById(idAdherant)
                    .orElseThrow(() -> new IllegalArgumentException("Adhérant non trouvé"));

            // Initialiser les abonnements pour éviter LazyInitializationException
            adherant.getAbonnements().size();
            logger.debug("Adhérant ID: {}, Nom: {}, Abonnements: {}", 
                        adherant.getIdAdherant(), adherant.getNom() + " " + adherant.getPrenom(), 
                        adherant.getAbonnements().size());

            // Construire les informations de l'adhérant
            Map<String, Object> adherantData = new LinkedHashMap<>();
            adherantData.put("nomPrenom", adherant.getNom() + " " + adherant.getPrenom());
            adherantData.put("dateNaissance", adherant.getDateNaissance().toString());
            adherantData.put("quotaRestantEmprunt", adherant.getQuotaRestant());
            adherantData.put("quotaRestantProlongement", adherant.getQuotaRestantProlongement());
            adherantData.put("quotaRestantReservation", adherant.getQuotaRestantReservation());
            adherantData.put("typeAdherant", adherant.getTypeAdherant().getNomType());

            // Construire les informations de l'abonnement
            Map<String, Object> abonnementData = new HashMap<>();
            boolean hasValidAbonnement = adherant.hasValidAbonnement(LocalDate.now());
            abonnementData.put("statut", hasValidAbonnement ? "valide" : "expiré");
            
            // Trouver l'abonnement actif (le plus récent si valide)
            Optional<Abonnement> activeAbonnement = adherant.getAbonnements().stream()
                    .filter(abonnement -> !LocalDate.now().isBefore(abonnement.getDateDebut()) 
                            && !LocalDate.now().isAfter(abonnement.getDateFin()))
                    .findFirst();
            if (activeAbonnement.isPresent()) {
                abonnementData.put("dateDebut", activeAbonnement.get().getDateDebut().toString());
                abonnementData.put("dateFin", activeAbonnement.get().getDateFin().toString());
            } else {
                abonnementData.put("dateDebut", null);
                abonnementData.put("dateFin", null);
            }
            
            adherantData.put("abonnement", abonnementData);
            adherantData.put("sanction", abonnementRepository.hasActivePenalite(idAdherant, LocalDate.now()) ? "oui" : "non");

            // Construire la réponse JSON
            Map<String, Object> response = new LinkedHashMap<>();
            response.put("adherant", adherantData);

            logger.info("Informations récupérées pour l'adhérant ID {}: nomPrenom={}, dateNaissance={}, typeAdherant={}, abonnement={}, quotas=[emprunt={}, prolongement={}, reservation={}], sanction={}", 
                        idAdherant, adherantData.get("nomPrenom"), adherantData.get("dateNaissance"), 
                        adherantData.get("typeAdherant"), abonnementData.get("statut"), 
                        adherantData.get("quotaRestantEmprunt"), adherantData.get("quotaRestantProlongement"), 
                        adherantData.get("quotaRestantReservation"), adherantData.get("sanction"));

            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            logger.error("Erreur lors de la récupération de l'adhérant ID {} : {}", idAdherant, e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.singletonMap("error", e.getMessage()));
        } catch (Exception e) {
            logger.error("Erreur inattendue lors de la récupération de l'adhérant ID {} : {}", idAdherant, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("error", "Erreur lors du chargement des informations de l'adhérant."));
        }
    }
}