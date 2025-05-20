package com.example.StudyHub.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HubResponse {
    private Long hubId;
    private String hubName;
    private String address;
    private String cityName;
    private int availableSeats;
    private String imageUrl;

}