package com.example.StudyHub.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class HubAvailability{
    private Long hubId;
    private String hubName;
    private String address;
    private Long availableSeatCount;
}