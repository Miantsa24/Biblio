
package com.example.biblio.model;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "TypeAdherant")
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

    @Column(name = "jours_penalites", nullable = false)
    private int joursPenalites;

    @Column(name = "jours_prets", nullable = false)
    private int joursPrets;

    @OneToMany(mappedBy = "typeAdherant")
    private Set<Adherant> adherants = new HashSet<>();

    public TypeAdherant() {}

    public TypeAdherant(String nomType, int quotaEmprunts, int quotaReservations, int quotaProlongements, int joursPenalites, int joursPrets) {
        this.nomType = nomType;
        this.quotaEmprunts = quotaEmprunts;
        this.quotaReservations = quotaReservations;
        this.quotaProlongements = quotaProlongements;
        this.joursPenalites = joursPenalites;
        this.joursPrets = joursPrets;
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

    public int getJoursPenalites() {
        return joursPenalites;
    }

    public void setJoursPenalites(int joursPenalites) {
        this.joursPenalites = joursPenalites;
    }

    public int getJoursPrets() {
        return joursPrets;
    }

    public void setJoursPrets(int joursPrets) {
        this.joursPrets = joursPrets;
    }

    public Set<Adherant> getAdherants() {
        return adherants;
    }

    public void setAdherants(Set<Adherant> adherants) {
        this.adherants = adherants;
    }
}
