package com.example.biblio.model;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "Typeadherant")
public class TypeAdherant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_type_adherant")
    private Integer id;

    @Column(name = "nom_type", nullable = false, unique = true)
    private String nomType;

    @Column(name = "quota_emprunts", nullable = false)
    private int quotaEmprunts;

    @Column(name = "quota_reservations", nullable = false)
    private int quotaReservations;

    @Column(name = "quota_prolongements", nullable = false)
    private int quotaProlongements;

    @OneToMany(mappedBy = "typeAdherant")
    private Set<Adherant> adherants = new HashSet<>();

    public TypeAdherant() {}

    public TypeAdherant(String nomType, int quotaEmprunts, int quotaReservations, int quotaProlongements) {
        this.nomType = nomType;
        this.quotaEmprunts = quotaEmprunts;
        this.quotaReservations = quotaReservations;
        this.quotaProlongements = quotaProlongements;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNomType() {
        return nomType;
    }

    public void setNomType(String nomType) {
        this.nomType = nomType;
    }

    public int getQuotaEmprunts() {
        return quotaEmprunts;
    }

    public void setQuotaEmprunts(int quotaEmprunts) {
        this.quotaEmprunts = quotaEmprunts;
    }

    public int getQuotaReservations() {
        return quotaReservations;
    }

    public void setQuotaReservations(int quotaReservations) {
        this.quotaReservations = quotaReservations;
    }

    public int getQuotaProlongements() {
        return quotaProlongements;
    }

    public void setQuotaProlongements(int quotaProlongements) {
        this.quotaProlongements = quotaProlongements;
    }

    public Set<Adherant> getAdherants() {
        return adherants;
    }

    public void setAdherants(Set<Adherant> adherants) {
        this.adherants = adherants;
    }
}
