package com.example.StudyHub.repository;

import com.example.StudyHub.model.Seat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SeatRepository extends JpaRepository<Seat, Long> {
}