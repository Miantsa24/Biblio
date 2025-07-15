package com.example.biblio.repository;

import com.example.biblio.model.Prolongement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProlongementRepository extends JpaRepository<Prolongement, Integer> {

    @Query("SELECT p FROM Prolongement p WHERE p.statut = 'EN_ATTENTE'")
    List<Prolongement> findAllPendingProlongements();

    @Query("SELECT p FROM Prolongement p WHERE p.pret.id = :pretId AND p.statut = :statut")
    List<Prolongement> findByPretIdAndStatut(@Param("pretId") Integer pretId, @Param("statut") Prolongement.StatutProlongement statut);
}