package com.example.StudyHub.repository;


import com.example.StudyHub.model.Availability;
import com.example.StudyHub.model.Seat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AvailabilityRepository extends JpaRepository<Availability, Long> {
    List<Availability> findBySeatInAndDate(List<Seat> seats, LocalDate date);
}