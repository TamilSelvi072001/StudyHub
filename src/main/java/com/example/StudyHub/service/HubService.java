package com.example.StudyHub.service;

import com.example.StudyHub.dto.HubResponse;
import com.example.StudyHub.dto.SeatResponse;
import com.example.StudyHub.dto.TableDetailsResponse;
import com.example.StudyHub.model.Hub;
import com.example.StudyHub.model.HubTable;
import com.example.StudyHub.model.Seat;
import com.example.StudyHub.repository.HubRepository;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HubService {

    private final HubRepository hubRepository;

    public List<HubResponse> getHubsWithAvailability(String city, LocalDate date) {
        List<Hub> hubs = hubRepository.findAllHubsByCity(city);

        return hubs.stream().map(hub -> {
            // Count the seats where there is an availability record and is available
            int availableSeats = (int) hub.getTables().stream()
                    .flatMap(table -> table.getSeats().stream()) // Flatten the seats for each table
                    .flatMap(seat -> seat.getAvailabilities().stream()) // Flatten the availabilities for each seat
                    .filter(avail -> avail.getDate().equals(date) && Boolean.TRUE.equals(avail.getIsAvailable())) // Filter by the date and availability status
                    .peek(avail -> System.out.println("Found availability: " + avail)) // Debug: print the availability data
                    .count();

            // Count seats that do not have any availability record for the given date (i.e., assume they are available)
            int seatsWithoutAvailability = (int) hub.getTables().stream()
                    .flatMap(table -> table.getSeats().stream())
                    .filter(seat -> seat.getAvailabilities().stream()
                            .noneMatch(avail -> avail.getDate().equals(date))) // No availability record for the given date
                    .peek(seat -> System.out.println("Found seat with no availability: " + seat.getSeatNumber())) // Debug: print seats with no availability data
                    .count();

            // Add the number of seats without availability records to the available seats count
            availableSeats += seatsWithoutAvailability;

            // Log unavailable seats for the given date (seats with availability records that are not available)
            hub.getTables().stream()
                    .flatMap(table -> table.getSeats().stream())
                    .forEach(seat -> seat.getAvailabilities().stream()
                            .filter(avail -> avail.getDate().equals(date) && Boolean.FALSE.equals(avail.getIsAvailable())) // Seats that are unavailable on the given date
                            .forEach(avail -> {
                                System.out.println("Not available seat: Table ID = " + seat.getTable().getTableId() +
                                        ", Seat Number = " + seat.getSeatNumber() +
                                        ", Date = " + avail.getDate());
                            })
                    );

            // Debug: print the final available seat count for the hub
            System.out.println("Available seats for hub " + hub.getHubName() + ": " + availableSeats);

            return new HubResponse(hub.getHubId(), hub.getHubName(), hub.getAddress(), availableSeats);
        }).collect(Collectors.toList());
    }

    public List<TableDetailsResponse> getHubDetails(Long hubId) {
        Hub hub = hubRepository.findById(hubId)
                .orElseThrow(() -> new RuntimeException("Hub not found"));

        List<TableDetailsResponse> tableDetails = new ArrayList<>();

        for (HubTable table : hub.getTables()) {
            List<SeatResponse> seatResponses = new ArrayList<>();
            for (Seat seat : table.getSeats()) {
                boolean isAvailable = seat.getAvailabilities().stream()
                        .anyMatch(a -> a.getDate().equals(LocalDate.now()) && a.getIsAvailable());
                seatResponses.add(new SeatResponse(seat.getSeatId(), isAvailable, seat.getSeatNumber()));            }
            tableDetails.add(new TableDetailsResponse(table.getTableId(), table.getTableNumber(), seatResponses));
        }

        return tableDetails;
    }
}