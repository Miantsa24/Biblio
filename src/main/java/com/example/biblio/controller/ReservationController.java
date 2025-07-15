package com.example.biblio.controller;

import com.example.biblio.model.Reservation;
import com.example.biblio.repository.AdherantRepository;
import com.example.biblio.repository.ExemplaireRepository;
import com.example.biblio.repository.LivreRepository;
import com.example.biblio.repository.ReservationRepository;
import com.example.biblio.service.ReservationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/reservations")
public class ReservationController {

    private static final Logger logger = LoggerFactory.getLogger(ReservationController.class);

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private AdherantRepository adherantRepository;

    @Autowired
    private ExemplaireRepository exemplaireRepository;

    @Autowired
    private LivreRepository livreRepository;

    @GetMapping("/liste")
    public String listReservations(Model model) {
        List<Reservation> reservationsEnAttente = reservationRepository.findAllPendingReservations();
        model.addAttribute("reservationsEnAttente", reservationsEnAttente);
        model.addAttribute("adherantRepository", adherantRepository);
        model.addAttribute("exemplaireRepository", exemplaireRepository);
        model.addAttribute("livreRepository", livreRepository);
        return "liste-reservations";
    }

    @PostMapping("/approuver")
    public String approuverReservation(@RequestParam Integer idReservation, Model model) {
        try {
            reservationService.approuverReservation(idReservation);
            return "redirect:/reservations/liste?success=approuver";
        } catch (IllegalArgumentException | IllegalStateException e) {
            logger.error("Erreur lors de l'approbation de la réservation : {}", e.getMessage());
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("reservationsEnAttente", reservationRepository.findAllPendingReservations());
            model.addAttribute("adherantRepository", adherantRepository);
            model.addAttribute("exemplaireRepository", exemplaireRepository);
            model.addAttribute("livreRepository", livreRepository);
            return "liste-reservations";
        }
    }

    @PostMapping("/refuser")
    public String refuserReservation(@RequestParam Integer idReservation, Model model) {
        try {
            reservationService.refuserReservation(idReservation);
            return "redirect:/reservations/liste?success=refuser";
        } catch (IllegalArgumentException | IllegalStateException e) {
            logger.error("Erreur lors du refus de la réservation : {}", e.getMessage());
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("reservationsEnAttente", reservationRepository.findAllPendingReservations());
            model.addAttribute("adherantRepository", adherantRepository);
            model.addAttribute("exemplaireRepository", exemplaireRepository);
            model.addAttribute("livreRepository", livreRepository);
            return "liste-reservations";
        }
    }

}