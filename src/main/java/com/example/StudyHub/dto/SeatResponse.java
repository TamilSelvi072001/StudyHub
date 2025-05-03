package com.example.StudyHub.dto;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SeatResponse {
    private Long seatId;
    private boolean isAvailable;
    private String seatNumber;
}