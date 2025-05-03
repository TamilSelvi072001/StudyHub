package com.example.StudyHub.dto;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class TableDetailsResponse {
    private Long tableId;
    private String tableNum;
    private List<SeatResponse> seats;



    public Long getTableId() {
        return tableId;
    }

    public List<SeatResponse> getSeats() {
        return seats;
    }
}