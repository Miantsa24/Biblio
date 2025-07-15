package com.example.biblio.repository;

import com.example.biblio.model.Adherant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface AdherantRepository extends JpaRepository<Adherant, Integer> {
    boolean existsByEmail(String email);

    boolean existsByMotDePasse(String motDePasse);

    Optional<Adherant> findByEmail(String email);

    @Query("SELECT a FROM Adherant a WHERE EXISTS (SELECT ab FROM a.abonnements ab WHERE ab.dateFin > :currentDate)")
    List<Adherant> findAllWithValidAbonnement(LocalDate currentDate);
}