package com.example.StudyHub.repository;

import com.example.StudyHub.model.Hub;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface HubRepository extends JpaRepository<Hub, Long> {

    @Query("SELECT h FROM Hub h WHERE h.city.cityName = :city")
    List<Hub> findAllHubsByCity(@Param("city") String city);
}