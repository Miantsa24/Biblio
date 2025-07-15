package com.example.biblio.repository;

import com.example.biblio.model.JourFerier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDate;

public interface JourFerierRepository extends JpaRepository<JourFerier, Integer> {
    @Query(value = "SELECT COUNT(*) FROM JourFerier WHERE date_ferier = :date", nativeQuery = true)
    Long countByDateFerier(@Param("date") LocalDate dateFerier);

    default boolean existsByDateFerier(LocalDate dateFerier) {
        return countByDateFerier(dateFerier) > 0;
    }
}