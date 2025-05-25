package com.example.StudyHub.controller;

import com.example.StudyHub.dto.SeatBlockRequest;
import com.example.StudyHub.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/api/book")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    public String bookSeats(@RequestBody SeatBlockRequest request) {
        bookingService.bookSeats(request);
        return "Seats booked successfully!";
    }
}