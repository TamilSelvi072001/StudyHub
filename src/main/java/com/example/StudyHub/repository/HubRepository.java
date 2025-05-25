package com.example.StudyHub.repository;

import com.example.StudyHub.model.Hub;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HubRepository extends JpaRepository<Hub, Long> {

    @Query("SELECT DISTINCT h FROM Hub h " +
            "LEFT JOIN FETCH h.tables t " +
            "LEFT JOIN FETCH t.seats s " +
            "LEFT JOIN FETCH s.availabilities a " +
            "WHERE h.cityName = :city")
    List<Hub> findAllHubsByCityWithDetails(@Param("city") String city);

    // Optionally override findById to fetch details for getHubDetails:
    @Query("SELECT h FROM Hub h " +
            "LEFT JOIN FETCH h.tables t " +
            "LEFT JOIN FETCH t.seats s " +
            "LEFT JOIN FETCH s.availabilities a " +
            "WHERE h.hubId = :hubId")
    Optional<Hub> findByIdWithDetails(@Param("hubId") Long hubId);
}