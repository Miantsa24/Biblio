package com.example.biblio.repository;

import com.example.biblio.model.TypeAdherant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface TypeAdherantRepository extends JpaRepository<TypeAdherant, Integer> {
    boolean existsByNomType(String nomType);
    Optional<TypeAdherant> findByNomType(String nomType);
}