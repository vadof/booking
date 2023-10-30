package com.reservation.backend.controllers;

import com.reservation.backend.dto.BookingDTO;
import com.reservation.backend.services.BookingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/booking")
public class BookingController {
    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @GetMapping("/history")
    public ResponseEntity<BookingDTO> getBookingsByUser(@RequestHeader("Authorization") String token) {
        Optional<BookingDTO> optionalBookingDTO = bookingService.getAllBookings(token);

        return optionalBookingDTO.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
}
