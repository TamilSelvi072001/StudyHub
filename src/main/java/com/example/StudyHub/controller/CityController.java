package com.example.StudyHub.controller;

import com.example.StudyHub.service.CityService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cities")
@CrossOrigin(origins = {
        "http://localhost:3000",
        "https://focus-hub-nine.vercel.app"
})
@Slf4j
public class CityController {

    private final CityService cityService;

    public CityController(CityService cityService) {
        this.cityService = cityService;
    }

    @GetMapping("/names")
    public List<String> getAllCityNames() {
        long start = System.currentTimeMillis();
        List<String> cityNames = cityService.getAllCityNames();
        log.info("Total API duration: {} ms", System.currentTimeMillis() - start);
        return cityNames;
    }
}