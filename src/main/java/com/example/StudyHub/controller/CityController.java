package com.example.StudyHub.controller;

import com.example.StudyHub.model.City;
import com.example.StudyHub.service.CityService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cities")
@CrossOrigin(origins = "http://localhost:3000") // Replace with your React frontend URL
public class CityController {

    private final CityService cityService;

    public CityController(CityService cityService) {
        this.cityService = cityService;
    }

    @GetMapping
    public List<City> getAllCities() {
        return cityService.getAllCities();
    }

    @GetMapping("/names")
    public List<String> getAllCityNames() {
        return cityService.getAllCityNames();
    }
}