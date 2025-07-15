package com.example.biblio.service;

import com.example.biblio.model.Penalite;
import com.example.biblio.repository.PenaliteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class PenaliteService {

    @Autowired
    private PenaliteRepository penaliteRepository;

    public List<Penalite> getPenalitesEnCours() {
        return penaliteRepository.findByDateFinPenaliteAfter(LocalDate.now());
    }
}