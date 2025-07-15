package com.example.biblio.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "Penalite")
public class Penalite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_penalite")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "id_adherant", nullable = false)
    private Adherant adherant;

    @ManyToOne
    @JoinColumn(name = "id_pret")
    private Pret pret;

    @Enumerated(EnumType.STRING)
    @Column(name = "type_penalite", nullable = false)
    private TypePenalite typePenalite;

    @Column(name = "date_debut_penalite", nullable = false)
    private LocalDate dateDebutPenalite;

    @Column(name = "nombre_jours", nullable = false)
    private Integer nombreJours;

    @Column(name = "date_fin_penalite", nullable = false)
    private LocalDate dateFinPenalite;

    public enum TypePenalite {
        RETARD
    }

    public Penalite() {}

    public Penalite(Adherant adherant, Pret pret, TypePenalite typePenalite, LocalDate dateDebutPenalite, Integer nombreJours, LocalDate dateFinPenalite) {
        this.adherant = adherant;
        this.pret = pret;
        this.typePenalite = typePenalite;
        this.dateDebutPenalite = dateDebutPenalite;
        this.nombreJours = nombreJours;
        this.dateFinPenalite = dateFinPenalite;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Adherant getAdherant() {
        return adherant;
    }

    public void setAdherant(Adherant adherant) {
        this.adherant = adherant;
    }

    public Pret getPret() {
        return pret;
    }

    public void setPret(Pret pret) {
        this.pret = pret;
    }

    public TypePenalite getTypePenalite() {
        return typePenalite;
    }

    public void setTypePenalite(TypePenalite typePenalite) {
        this.typePenalite = typePenalite;
    }

    public LocalDate getDateDebutPenalite() {
        return dateDebutPenalite;
    }

    public void setDateDebutPenalite(LocalDate dateDebutPenalite) {
        this.dateDebutPenalite = dateDebutPenalite;
    }

    public Integer getNombreJours() {
        return nombreJours;
    }

    public void setNombreJours(Integer nombreJours) {
        this.nombreJours = nombreJours;
    }

    public LocalDate getDateFinPenalite() {
        return dateFinPenalite;
    }

    public void setDateFinPenalite(LocalDate dateFinPenalite) {
        this.dateFinPenalite = dateFinPenalite;
    }
}