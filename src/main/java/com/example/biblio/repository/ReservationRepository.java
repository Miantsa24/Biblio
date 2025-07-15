package com.example.biblio.repository;

import com.example.biblio.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Integer> {

    @Query("SELECT COUNT(r) > 0 FROM Reservation r WHERE r.idExemplaire = :exemplaireId AND r.statut IN ('EN_ATTENTE', 'HONOREE')")
    boolean hasActiveReservation(@Param("exemplaireId") Integer exemplaireId);

    @Query("SELECT COUNT(r) > 0 FROM Reservation r WHERE r.idAdherant = :idAdherant AND r.idExemplaire = :exemplaireId AND r.statut IN ('EN_ATTENTE', 'HONOREE')")
    boolean hasActiveReservationByAdherantAndExemplaire(@Param("idAdherant") Integer idAdherant, @Param("exemplaireId") Integer exemplaireId);

    @Query("SELECT COUNT(r) > 0 FROM Reservation r WHERE r.idExemplaire = :exemplaireId AND r.statut = 'HONOREE'")
    boolean hasHonoredReservation(@Param("exemplaireId") Integer exemplaireId);

    @Query("SELECT COUNT(r) > 0 FROM Reservation r WHERE r.idExemplaire = :exemplaireId AND r.statut = 'EN_ATTENTE' AND r.dateDemande < :dateDemande")
    boolean hasActiveReservationBeforeDate(@Param("exemplaireId") Integer exemplaireId, @Param("dateDemande") LocalDateTime dateDemande);

    @Query("SELECT COUNT(r) FROM Reservation r WHERE r.idAdherant = :idAdherant AND r.statut = 'EN_ATTENTE'")
    int countActiveReservationsByAdherantId(@Param("idAdherant") Integer idAdherant);

    @Query("SELECT r FROM Reservation r WHERE r.idExemplaire = :exemplaireId AND r.statut = 'EN_ATTENTE' ORDER BY r.dateDemande ASC, r.id ASC")
    Reservation findFirstActiveReservationByExemplaireId(@Param("exemplaireId") Integer exemplaireId);

    @Query("SELECT r FROM Reservation r WHERE r.statut = 'EN_ATTENTE' ORDER BY r.dateDemande ASC, r.id ASC")
    List<Reservation> findAllPendingReservations();

    @Query("SELECT r FROM Reservation r WHERE r.idAdherant = :idAdherant AND r.statut = :statut")
    List<Reservation> findAllByAdherantIdAndStatut(@Param("idAdherant") Integer idAdherant, @Param("statut") Reservation.StatutReservation statut);

    @Query("SELECT r FROM Reservation r WHERE r.idExemplaire = :exemplaireId AND r.statut IN ('EN_ATTENTE', 'HONOREE') " +
           "ORDER BY CASE WHEN r.statut = 'HONOREE' THEN 0 ELSE 1 END, r.dateDemande ASC, r.id ASC FETCH FIRST 1 ROWS ONLY")
    Reservation findTopPriorityReservationByExemplaireId(@Param("exemplaireId") Integer exemplaireId);

    List<Reservation> findByIdAdherant(Integer idAdherant);
}