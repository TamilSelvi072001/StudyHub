package com.example.StudyHub.controller;

import com.example.StudyHub.dto.TableDetailsResponse;
import com.example.StudyHub.service.HubService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/hubdetails")
public class HubDetailsController {

    private final HubService hubService;

    public HubDetailsController(HubService hubService) {
        this.hubService = hubService;
    }

//    @GetMapping("/{hubId}")
//    public ResponseEntity<List<TableDetailsResponse>> getHubDetails(@PathVariable Long hubId) {
//        List<TableDetailsResponse> tableDetails = hubService.getHubDetails(hubId);
//        return ResponseEntity.ok(tableDetails);
//    }
}