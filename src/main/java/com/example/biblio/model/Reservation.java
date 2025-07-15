package com.example.biblio.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_reservation")
    private Integer idReservation;

    @Column(name = "id_exemplaire")
    private Integer idExemplaire;

    @Column(name = "id_adherant")
    private Integer idAdherant;

    @Column(name = "date_reservation")
    private LocalDate dateReservation;

    @Column(name = "date_demande")
    private LocalDateTime dateDemande;

    @Enumerated(EnumType.STRING)
    private StatutReservation statut;

    @Transient
    private Exemplaire exemplaire; // Champ transitoire pour stocker l'exemplaire

    public enum StatutReservation {
        EN_ATTENTE, HONOREE, ANNULEE, TERMINEE
    }

    // Getters and setters
    public Integer getId() {
        return idReservation;
    }

    public void setId(Integer idReservation) {
        this.idReservation = idReservation;
    }

    public Integer getIdExemplaire() {
        return idExemplaire;
    }

    public void setIdExemplaire(Integer idExemplaire) {
        this.idExemplaire = idExemplaire;
    }

    public Integer getIdAdherant() {
        return idAdherant;
    }

    public void setIdAdherant(Integer idAdherant) {
        this.idAdherant = idAdherant;
    }

    public LocalDate getDateReservation() {
        return dateReservation;
    }

    public void setDateReservation(LocalDate dateReservation) {
        this.dateReservation = dateReservation;
    }

    public LocalDateTime getDateDemande() {
        return dateDemande;
    }

    public void setDateDemande(LocalDateTime dateDemande) {
        this.dateDemande = dateDemande;
    }

    public StatutReservation getStatut() {
        return statut;
    }

    public void setStatut(StatutReservation statut) {
        this.statut = statut;
    }

    public Exemplaire getExemplaire() {
        return exemplaire;
    }

    public void setExemplaire(Exemplaire exemplaire) {
        this.exemplaire = exemplaire;
    }
}