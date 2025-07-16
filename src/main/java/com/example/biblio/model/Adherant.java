package com.example.biblio.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "Adherant")
public class Adherant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_adherant")
    private Integer idAdherant;

    @Column(name = "nom", nullable = false)
    private String nom;

    @Column(name = "prenom", nullable = false)
    private String prenom;

    @Column(name = "date_naissance", nullable = false)
    private LocalDate dateNaissance;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "mot_de_passe", nullable = false)
    private String motDePasse;

    @ManyToOne
    @JoinColumn(name = "id_type_adherant", nullable = false)
    private TypeAdherant typeAdherant;

    @Column(name = "quota_restant")
    private Integer quotaRestant;

    @Column(name = "quota_restant_reservation")
    private Integer quotaRestantReservation;

    @Column(name = "quota_restant_prolongement")
    private Integer quotaRestantProlongement;

    @OneToMany(mappedBy = "adherant")
    private List<Abonnement> abonnements;

    public Adherant() {}

    public Adherant(String nom, String prenom, LocalDate dateNaissance, String email, TypeAdherant typeAdherant, String motDePasse) {
        this.nom = nom;
        this.prenom = prenom;
        this.dateNaissance = dateNaissance;
        this.email = email;
        this.typeAdherant = typeAdherant;
        this.motDePasse = motDePasse;
    }

    public Integer getIdAdherant() {
        return idAdherant;
    }

    public void setIdAdherant(Integer idAdherant) {
        this.idAdherant = idAdherant;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public LocalDate getDateNaissance() {
        return dateNaissance;
    }

    public void setDateNaissance(LocalDate dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMotDePasse() {
        return motDePasse;
    }

    public void setMotDePasse(String motDePasse) {
        this.motDePasse = motDePasse;
    }

    public TypeAdherant getTypeAdherant() {
        return typeAdherant;
    }

    public void setTypeAdherant(TypeAdherant typeAdherant) {
        this.typeAdherant = typeAdherant;
    }

    public Integer getQuotaRestant() {
        return quotaRestant;
    }

    public void setQuotaRestant(Integer quotaRestant) {
        this.quotaRestant = quotaRestant;
    }

    public Integer getQuotaRestantReservation() {
        return quotaRestantReservation;
    }

    public void setQuotaRestantReservation(Integer quotaRestantReservation) {
        this.quotaRestantReservation = quotaRestantReservation;
    }

    public List<Abonnement> getAbonnements() {
        return abonnements;
    }

    public void setAbonnements(List<Abonnement> abonnements) {
        this.abonnements = abonnements;
    }


    public Integer getQuotaRestantProlongement() {
        return quotaRestantProlongement;
    }

    public void setQuotaRestantProlongement(Integer quotaRestantProlongement) {
        this.quotaRestantProlongement = quotaRestantProlongement;
    }

    public boolean hasValidAbonnement(LocalDate date) {
        if (abonnements == null) return false;
        return abonnements.stream()
                .anyMatch(abonnement -> !date.isBefore(abonnement.getDateDebut()) && !date.isAfter(abonnement.getDateFin()));
    }
}