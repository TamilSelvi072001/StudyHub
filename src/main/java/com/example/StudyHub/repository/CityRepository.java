package com.example.StudyHub.repository;

import com.example.StudyHub.model.City;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CityRepository extends JpaRepository<City, Long> {
}