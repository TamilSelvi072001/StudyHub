package com.example.StudyHub.controller;

import com.example.StudyHub.dto.BookingResponse;
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
    public BookingResponse bookSeats(@RequestBody SeatBlockRequest request) {
        return bookingService.bookSeats(request);
    }
}