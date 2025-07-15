package com.example.biblio.model;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "Livre")
public class Livre {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_livre")
    private Integer id;

    @Column(name = "titre", nullable = false)
    private String titre;

    @Column(name = "auteur")
    private String auteur;

    @Column(name = "age_minimum", nullable = false)
    private int ageMinimum;

    @Column(name = "isbn", unique = true)
    private String isbn;

    @OneToMany(mappedBy = "livre")
    private Set<Exemplaire> exemplaires = new HashSet<>();

    public Livre() {}

    public Livre(String titre, String auteur, int ageMinimum, String isbn) {
        this.titre = titre;
        this.auteur = auteur;
        this.ageMinimum = ageMinimum;
        this.isbn = isbn;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getAuteur() {
        return auteur;
    }

    public void setAuteur(String auteur) {
        this.auteur = auteur;
    }

    public int getAgeMinimum() {
        return ageMinimum;
    }

    public void setAgeMinimum(int ageMinimum) {
        this.ageMinimum = ageMinimum;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public Set<Exemplaire> getExemplaires() {
        return exemplaires;
    }

    public void setExemplaires(Set<Exemplaire> exemplaires) {
        this.exemplaires = exemplaires;
    }
}