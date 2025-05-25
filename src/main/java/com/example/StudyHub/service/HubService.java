package com.example.StudyHub.service;

import com.example.StudyHub.dto.HubResponse;
import com.example.StudyHub.dto.SeatResponse;
import com.example.StudyHub.dto.TableDetailsResponse;
import com.example.StudyHub.model.Hub;
import com.example.StudyHub.model.HubTable;
import com.example.StudyHub.model.Seat;
import com.example.StudyHub.repository.HubRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HubService {

    private final HubRepository hubRepository;

    private static final Logger logger = LoggerFactory.getLogger(HubService.class);

    public List<HubResponse> getHubsWithAvailability(String city, LocalDate date) {
        long start = System.currentTimeMillis();

        List<Hub> hubs = hubRepository.findAllHubsByCityWithDetails(city);

        long end = System.currentTimeMillis();
        logger.info("DB call findAllHubsByCityWithDetails took {} ms", (end - start));

        return hubs.stream().map(hub -> {
            List<Seat> allSeats = hub.getTables().stream()
                    .flatMap(table -> table.getSeats().stream())
                    .toList();

            long availableSeats = allSeats.stream()
                    .filter(seat -> seatAvailableOnDate(seat, date))
                    .count();

            long seatsWithoutAvailability = allSeats.stream()
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
                    (int) availableSeats,
                    imageUrl
            );
        }).toList();
    }

    private boolean seatAvailableOnDate(Seat seat, LocalDate date) {
        return seat.getAvailabilities().stream()
                .filter(avail -> avail.getDate().equals(date))
                .map(avail -> avail.isAvailable())
                .findFirst()
                .orElse(true);
    }

//    public List<TableDetailsResponse> getHubDetails(Long hubId) {
//        Hub hub = hubRepository.findById(hubId)
//                .orElseThrow(() -> new RuntimeException("Hub not found"));
//
//        List<TableDetailsResponse> tableDetails = new ArrayList<>();
//
//        for (HubTable table : hub.getTables()) {
//            List<SeatResponse> seatResponses = new ArrayList<>();
//            for (Seat seat : table.getSeats()) {
//                boolean isAvailable = seat.getAvailabilities().stream()
//                        .anyMatch(a -> a.getDate().equals(LocalDate.now()) && a.isAvailable());
//                seatResponses.add(new SeatResponse(seat.getSeatId(), isAvailable, seat.getSeatNumber()));            }
//            tableDetails.add(new TableDetailsResponse(table.getTableId(), table.getTableNumber(), seatResponses));
//        }
//
//        return tableDetails;
//    }
}