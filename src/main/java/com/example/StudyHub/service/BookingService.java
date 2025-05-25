package com.example.StudyHub.service;

import com.example.StudyHub.dto.BookingResponse;
import com.example.StudyHub.dto.SeatBlockRequest;
import com.example.StudyHub.model.*;
import com.example.StudyHub.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
@RequiredArgsConstructor
@Service
public class BookingService {
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final HubRepository hubRepository;
    private final SeatRepository seatRepository;

    public BookingResponse bookSeats(SeatBlockRequest request) {
        // Get user
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Get hub
        Hub hub = hubRepository.findById(request.getHubId())
                .orElseThrow(() -> new RuntimeException("Hub not found"));

        // Prepare seats list
        List<BookingResponse.SeatInfo> seatInfoList = new ArrayList<>();

        for (SeatBlockRequest.SeatDetail seatReq : request.getSeats()) {
            Seat seat = seatRepository.findById(seatReq.getSeatId())
                    .orElseThrow(() -> new RuntimeException("Seat not found"));

            // Save booking (optional logic here)
            Booking booking = new Booking();
            booking.setEmail(user.getEmail());
            booking.setDate(request.getDate());
            booking.setSeat(seat);
            booking.setTable(seat.getTable());
            booking.setHub(hub);
            bookingRepository.save(booking);

            seatInfoList.add(new BookingResponse.SeatInfo(
                    seat.getTable().getTableId().intValue(),
                    Integer.parseInt(seat.getSeatNumber().replaceAll("[^0-9]", "")) // assume seatNumber like "S1"
            ));
        }

        return new BookingResponse(
                user.getUserName(),
                user.getEmail(),
                user.getPhone(),
                hub.getAddress(),
                request.getDate(),
                seatInfoList
        );
    }
}