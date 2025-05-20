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
        List<Hub> hubs = hubRepository.findAllHubsByCity(city); // Use your actual method here

        return hubs.stream().map(hub -> {
            int availableSeats = (int) hub.getTables().stream()
                    .flatMap(table -> table.getSeats().stream())
                    .flatMap(seat -> seat.getAvailabilities().stream())
                    .filter(avail -> avail.getDate().equals(date) && Boolean.TRUE.equals(avail.isAvailable()))
                    .count();

            int seatsWithoutAvailability = (int) hub.getTables().stream()
                    .flatMap(table -> table.getSeats().stream())
                    .filter(seat -> seat.getAvailabilities().stream()
                            .noneMatch(avail -> avail.getDate().equals(date)))
                    .count();

            availableSeats += seatsWithoutAvailability;

            int imageIndex = (int) (hub.getHubId() % 9) + 1;
            String imageUrl = "/images/hub" + imageIndex + ".jpg";

            return new HubResponse(
                    hub.getHubId(),
                    hub.getHubName(),
                    hub.getAddress(),
                    hub.getCityName(),
                    availableSeats,
                    imageUrl
            );
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
                        .anyMatch(a -> a.getDate().equals(LocalDate.now()) && a.isAvailable());
                seatResponses.add(new SeatResponse(seat.getSeatId(), isAvailable, seat.getSeatNumber()));            }
            tableDetails.add(new TableDetailsResponse(table.getTableId(), table.getTableNumber(), seatResponses));
        }

        return tableDetails;
    }
}