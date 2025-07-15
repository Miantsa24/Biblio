package com.example.biblio.repository;

import com.example.biblio.model.Pret;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PretRepository extends JpaRepository<Pret, Integer> {
    // Vérifie si l'exemplaire est actuellement emprunté (disponibilité)
    Pret findByExemplaireIdAndDateRetourReelleIsNull(Integer exemplaireId);

    // Vérifie si l'adhérant a des pénalités actives
    @Query("SELECT COUNT(p) > 0 FROM Penalite p WHERE p.adherant.idAdherant = :adherantId AND p.dateFinPenalite >= :currentDate")
    boolean hasActivePenalties(Integer adherantId, java.time.LocalDate currentDate);

    // Vérifie si un prêt existe pour un adhérant et un exemplaire avec date_retour_reelle null
    @Query("SELECT p FROM Pret p WHERE p.adherant.idAdherant = :adherantId AND p.exemplaire.id = :exemplaireId AND p.dateRetourReelle IS NULL")
    Pret findByAdherantIdAndExemplaireIdAndDateRetourReelleIsNull(Integer adherantId, Integer exemplaireId);

    // Récupère le dernier prêt rendu pour un exemplaire
    @Query("SELECT p FROM Pret p WHERE p.exemplaire.id = :exemplaireId AND p.dateRetourReelle IS NOT NULL ORDER BY p.dateRetourReelle DESC")
    Pret findByExemplaireIdAndDateRetourReelleIsNotNull(Integer exemplaireId);

    // Récupère tous les prêts en cours (date_retour_reelle IS NULL)
    @Query("SELECT p FROM Pret p WHERE p.dateRetourReelle IS NULL")
    List<Pret> findAllByDateRetourReelleIsNull();

    // Récupère les prêts en cours pour un adhérant spécifique
    @Query("SELECT p FROM Pret p WHERE p.adherant.idAdherant = :adherantId AND p.dateRetourReelle IS NULL")
    List<Pret> findByAdherantIdAndDateRetourReelleIsNull(@Param("adherantId") Integer adherantId);

    // Compte les prêts en cours pour un adhérant
    long countByAdherantIdAdherantAndDateRetourReelleIsNull(Integer adherantId);
}