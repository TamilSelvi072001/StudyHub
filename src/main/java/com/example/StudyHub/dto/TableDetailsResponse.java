package com.example.StudyHub.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TableDetailsResponse {
    private Long tableId;
    private String tableNumber;
    private List<SeatResponse> seats;
}