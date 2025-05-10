package com.example.StudyHub.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class HubResponse {
    private Long hubId;
    private String hubName;
    private String address;
    private String cityName;
    private int availableSeats; // changed from boolean to int
}