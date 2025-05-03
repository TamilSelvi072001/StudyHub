package com.example.StudyHub.service;

import com.example.StudyHub.model.Hub;
import com.example.StudyHub.dto.HubAvailability;
import com.example.StudyHub.repository.HubRepository;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HubService {

    private final HubRepository hubRepository;

    public List<HubAvailability> getHubsByCityAndDate(String city, LocalDate date) {
        List<Hub> hubs = hubRepository.findHubsWithAvailableSeatsByCityAndDate(city, date);

        return hubs.stream().map(hub -> {
            int availableSeats = hub.getTables().stream()
                    .flatMap(table -> table.getSeats().stream())
                    .mapToInt(seat ->
                            (int) seat.getAvailability().stream()
                                    .filter(a -> a.getDate().equals(date) && a.getIsAvailable())                                    .count()
                    ).sum();

            return new HubAvailability(
                    hub.getHubId(),
                    hub.getHubName(),
                    hub.getAddress(),
                    (long) availableSeats
            );
        }).collect(Collectors.toList());
    }
}