package com.example.biblio.repository;

import com.example.biblio.model.Abonnement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface AbonnementRepository extends JpaRepository<Abonnement, Integer> {
    @Query("SELECT a FROM Abonnement a WHERE a.adherant.id = :adherantId AND a.dateFin >= :date ORDER BY a.dateFin DESC")
    Optional<Abonnement> findByAdherantIdAndDateFinAfter(Integer adherantId, LocalDate date);

    @Query("SELECT COUNT(p) > 0 FROM Penalite p WHERE p.adherant.id = :adherantId AND p.dateFinPenalite >= :currentDate")
    boolean hasActivePenalite(Integer adherantId, LocalDate currentDate);
}