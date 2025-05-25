package com.example.StudyHub.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.example.StudyHub.model.City;
import java.util.List;

public interface CityRepository extends JpaRepository<City, Long> {

    @Query("SELECT c.cityName FROM City c")
    List<String> findAllCityNames();
}