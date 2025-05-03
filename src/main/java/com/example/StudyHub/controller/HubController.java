package com.example.StudyHub.controller;

import com.example.StudyHub.dto.HubAvailability;
import com.example.StudyHub.service.HubService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/hub")
@RequiredArgsConstructor
public class HubController {

    private final HubService hubService;

    @GetMapping
    public List<HubAvailability> getHubs(
            @RequestParam String city,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        return hubService.getHubsByCityAndDate(city, date);
    }
}