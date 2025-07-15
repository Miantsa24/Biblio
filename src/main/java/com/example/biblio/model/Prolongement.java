package com.example.biblio.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "Prolongement")
public class Prolongement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_prolongement")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "id_pret", nullable = false)
    private Pret pret;

    @ManyToOne
    @JoinColumn(name = "id_adherant", nullable = false)
    private Adherant adherant;

    @ManyToOne
    @JoinColumn(name = "id_exemplaire", nullable = false)
    private Exemplaire exemplaire;

    @Column(name = "date_demande")
    private LocalDateTime dateDemande;

    @Column(name = "nouvelle_date_retour_prevue", nullable = false)
    private LocalDate nouvelleDateRetourPrevue;

    @Enumerated(EnumType.STRING)
    @Column(name = "statut", nullable = false)
    private StatutProlongement statut = StatutProlongement.EN_ATTENTE;

    public enum StatutProlongement {
        EN_ATTENTE, APPROUVE, REFUSE
    }

    // Constructeurs
    public Prolongement() {}

    public Prolongement(Pret pret, Adherant adherant, Exemplaire exemplaire, LocalDateTime dateDemande, LocalDate nouvelleDateRetourPrevue) {
        this.pret = pret;
        this.adherant = adherant;
        this.exemplaire = exemplaire;
        this.dateDemande = dateDemande;
        this.nouvelleDateRetourPrevue = nouvelleDateRetourPrevue;
    }

    // Getters et Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Pret getPret() {
        return pret;
    }

    public void setPret(Pret pret) {
        this.pret = pret;
    }

    public Adherant getAdherant() {
        return adherant;
    }

    public void setAdherant(Adherant adherant) {
        this.adherant = adherant;
    }

    public Exemplaire getExemplaire() {
        return exemplaire;
    }

    public void setExemplaire(Exemplaire exemplaire) {
        this.exemplaire = exemplaire;
    }

    public LocalDateTime getDateDemande() {
        return dateDemande;
    }

    public void setDateDemande(LocalDateTime dateDemande) {
        this.dateDemande = dateDemande;
    }


    public LocalDate getNouvelleDateRetourPrevue() {
        return nouvelleDateRetourPrevue;
    }

    public void setNouvelleDateRetourPrevue(LocalDate nouvelleDateRetourPrevue) {
        this.nouvelleDateRetourPrevue = nouvelleDateRetourPrevue;
    }

    public StatutProlongement getStatut() {
        return statut;
    }

    public void setStatut(StatutProlongement statut) {
        this.statut = statut;
    }
}