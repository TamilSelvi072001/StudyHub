package com.example.StudyHub.controller;

import com.example.StudyHub.dto.HubResponse;
import com.example.StudyHub.dto.TableDetailsResponse;
import com.example.StudyHub.service.HubService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;


@RestController
@RequestMapping("/hub")
@RequiredArgsConstructor
public class HubController {

    private final HubService hubService;

    @GetMapping
    public List<HubResponse> getHubs(
            @RequestParam String city,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        long start = System.currentTimeMillis();
        List<HubResponse> response = hubService.getHubsWithAvailability(city, date);
        long end = System.currentTimeMillis();
        System.out.println("[Controller] getHubs executed in " + (end - start) + " ms");
        return response;
    }


}