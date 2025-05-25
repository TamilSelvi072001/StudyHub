package com.example.StudyHub.service;

import com.example.StudyHub.dto.SeatBlockRequest;
import com.example.StudyHub.model.*;
import com.example.StudyHub.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final BookingRepository bookingRepository;
    private final SeatRepository seatRepository;
    private final HubTableRepository tableRepository;
    private final HubRepository hubRepository;
    private final AvailabilityRepository availabilityRepository;

    public void bookSeats(SeatBlockRequest request) {
        LocalDate date = request.getDate();
        String email = request.getEmail();

        for (SeatBlockRequest.SeatDetail seatDetail : request.getSeats()) {
            Seat seat = seatRepository.findById(seatDetail.getSeatId())
                    .orElseThrow(() -> new RuntimeException("Seat not found"));
            HubTable table = tableRepository.findById(seatDetail.getTableId())
                    .orElseThrow(() -> new RuntimeException("Table not found"));
            Hub hub = table.getHub(); // Get hub from table

            // 1. Save booking
            Booking booking = new Booking();
            booking.setEmail(email);
            booking.setDate(date);
            booking.setSeat(seat);
            booking.setTable(table);
            booking.setHub(hub);
            bookingRepository.save(booking);

            // 2. Update availability
            Availability availability = new Availability();
            availability.setDate(date);
            availability.setSeat(seat);
            availability.setAvailable(false);
            availabilityRepository.save(availability);
        }
    }
}