package com.example.StudyHub.service;

import com.example.StudyHub.repository.CityRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class CityService {

    private final CityRepository cityRepository;

    public CityService(CityRepository cityRepository) {
        this.cityRepository = cityRepository;
    }

    public List<String> getAllCityNames() {
        long start = System.currentTimeMillis();
        List<String> names = cityRepository.findAllCityNames();
        log.info("⏱️ DB query duration: {} ms", System.currentTimeMillis() - start);
        return names;
    }
}