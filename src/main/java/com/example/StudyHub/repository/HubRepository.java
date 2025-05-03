package com.example.StudyHub.repository;

import com.example.StudyHub.model.Hub;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface HubRepository extends JpaRepository<Hub, Long> {

    @Query("""
        SELECT h FROM Hub h
        WHERE h.city.cityName = :city
        AND EXISTS (
            SELECT s FROM Seat s
            JOIN s.table t
            WHERE t.hub = h
            AND EXISTS (
                SELECT a FROM Availability a
                WHERE a.seat = s AND a.date = :date AND a.isAvailable = true
            )
        )
    """)
    List<Hub> findHubsWithAvailableSeatsByCityAndDate(@Param("city") String city,
                                                      @Param("date") LocalDate date);
}