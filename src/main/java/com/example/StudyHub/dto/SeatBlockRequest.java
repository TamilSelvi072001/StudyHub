package com.example.StudyHub.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class SeatBlockRequest {
    private String email;
    private List<SeatDetail> seats;
    private LocalDate date;
    private Long hubId;

    @Getter
    @Setter
    public static class SeatDetail {
        private Long tableId;
        private Long seatId;
    }
}