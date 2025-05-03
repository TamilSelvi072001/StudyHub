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
        return hubService.getHubsWithAvailability(city, date);
    }

    @RestController
    @RequestMapping("/hubdetails")
    public class HubDetailsController {

        private final HubService hubService;

        public HubDetailsController(HubService hubService) {
            this.hubService = hubService;
        }

        @GetMapping("/{hubId}")
        public ResponseEntity<List<TableDetailsResponse>> getHubDetails(@PathVariable Long hubId) {
            List<TableDetailsResponse> tableDetails = hubService.getHubDetails(hubId);
            return ResponseEntity.ok(tableDetails);
        }
    }
}