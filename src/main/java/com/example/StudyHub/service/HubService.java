package com.example.StudyHub.service;

import com.example.StudyHub.dto.HubResponse;
import com.example.StudyHub.model.Hub;
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

    public List<HubResponse> getHubsWithAvailability(String city, LocalDate date) {
        List<Hub> hubs = hubRepository.findAllHubsByCity(city);

        return hubs.stream().map(hub -> {
            boolean hasAvailability = hub.getTables().stream()
                    .flatMap(table -> table.getSeats().stream())
                    .flatMap(seat -> seat.getAvailabilities().stream())
                    .anyMatch(avail -> avail.getDate().equals(date) && avail.getIsAvailable());

            return new HubResponse(hub.getHubId(), hub.getHubName(), hub.getAddress(), hasAvailability);
        }).collect(Collectors.toList());
    }}