package com.example.StudyHub.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

public class SeatBlockRequest {
    private String email; // optional unless storing user info
    private List<SeatDetail> seats;
    private LocalDate date;

    @Getter
    @Setter
    public static class SeatDetail {
        private Long tableId;
        private Long seatId;
        private int seatNumber;
    }
}