package com.example.StudyHub.service;

import com.example.StudyHub.model.City;
import com.example.StudyHub.repository.CityRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CityService {

    private final CityRepository cityRepository;

    public CityService(CityRepository cityRepository) {
        this.cityRepository = cityRepository;
    }

    public List<City> getAllCities() {
        return cityRepository.findAll();
    }
    // CityService.java
    public List<String> getAllCityNames() {
        return cityRepository.findAll()
                .stream()
                .map(City::getCityName)
                .toList();
    }
}