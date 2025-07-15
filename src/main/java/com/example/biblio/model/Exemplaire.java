package com.example.biblio.model;

import jakarta.persistence.*;

@Entity
@Table(name = "Exemplaire")
public class Exemplaire {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_exemplaire")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_livre", nullable = false)
    private Livre livre;

    @Enumerated(EnumType.STRING)
    @Column(name = "statut", nullable = false)
    private StatutExemplaire statut = StatutExemplaire.DISPONIBLE;

    public enum StatutExemplaire {
        DISPONIBLE, EMPRUNTE, RESERVE
    }

    public Exemplaire() {}

    public Exemplaire(Livre livre, StatutExemplaire statut) {
        this.livre = livre;
        this.statut = statut;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Livre getLivre() {
        return livre;
    }

    public void setLivre(Livre livre) {
        this.livre = livre;
    }

    public Integer getIdLivre() {
        return livre != null ? livre.getId() : null;
    }

    public void setIdLivre(Integer idLivre) {
        if (idLivre != null) {
            this.livre = new Livre();
            this.livre.setId(idLivre);
        } else {
            this.livre = null;
        }
    }

    public StatutExemplaire getStatut() {
        return statut;
    }

    public void setStatut(StatutExemplaire statut) {
        this.statut = statut;
    }

    
}