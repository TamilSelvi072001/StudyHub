package com.example.StudyHub.service;

import com.example.StudyHub.dto.SeatResponse;
import com.example.StudyHub.dto.TableDetailsResponse;
import com.example.StudyHub.model.Availability;
import com.example.StudyHub.model.HubTable;
import com.example.StudyHub.model.Seat;
import com.example.StudyHub.repository.AvailabilityRepository;
import com.example.StudyHub.repository.HubTableRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HubTableService {

    private final HubTableRepository hubTableRepository;
    private final AvailabilityRepository availabilityRepository;

    public List<TableDetailsResponse> getTableDetailsByHubIdAndDate(Long hubId, String dateStr) {
        // Step 1: Fetch all tables and seats for the hub
        List<HubTable> tables = hubTableRepository.findByHubIdWithSeats(hubId);

        // Step 2: Parse date string to LocalDate
        LocalDate date = LocalDate.parse(dateStr);

        // Step 3: Flatten all seats
        List<Seat> allSeats = tables.stream()
                .flatMap(table -> table.getSeats().stream())
                .collect(Collectors.toList());

        // Step 4: Fetch availability for the date
        List<Availability> availabilities = availabilityRepository.findBySeatInAndDate(allSeats, date);

        // Step 5: Map seatId -> isAvailable
        Map<Long, Boolean> seatAvailabilityMap = availabilities.stream()
                .collect(Collectors.toMap(
                        a -> a.getSeat().getSeatId(),
                        Availability::isAvailable
                ));

        // Step 6: Build response
        return tables.stream().map(table -> {
            List<SeatResponse> seatResponses = table.getSeats().stream().map(seat -> {
                boolean isAvailable = seatAvailabilityMap.getOrDefault(seat.getSeatId(), true);
                return new SeatResponse(seat.getSeatId(), isAvailable, seat.getSeatNumber());
            }).collect(Collectors.toList());

            return new TableDetailsResponse(
                    table.getTableId(),
                    table.getTableNumber(),
                    seatResponses
            );
        }).collect(Collectors.toList());
    }
}
