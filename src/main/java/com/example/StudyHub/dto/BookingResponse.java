package com.example.StudyHub.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingResponse {
    private String name;
    private String email;
    private String phone;
    private String hubAddress;
    private LocalDate date;
    private List<SeatInfo> seats;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SeatInfo {
        private int tableId;
        private int seatNumber;
    }
}