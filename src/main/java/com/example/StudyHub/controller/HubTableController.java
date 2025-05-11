package com.example.StudyHub.controller;

import com.example.StudyHub.dto.TableDetailsResponse;
import com.example.StudyHub.service.HubTableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/hubdetails")
public class HubTableController {

    @Autowired
    private HubTableService hubTableService;

    // Modified method to accept the date as a query parameter
    @GetMapping("/{hubId}")
    public List<TableDetailsResponse> getTablesByHubId(
            @PathVariable Long hubId,
            @RequestParam String date) {
        return hubTableService.getTableDetailsByHubIdAndDate(hubId, date);
    }
}