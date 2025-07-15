package com.example.biblio.repository;

import com.example.biblio.model.Penalite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PenaliteRepository extends JpaRepository<Penalite, Integer> {
    boolean existsByAdherantIdAdherantAndDateFinPenaliteAfter(Integer idAdherant, LocalDate date);
    List<Penalite> findByDateFinPenaliteAfter(LocalDate date);
}