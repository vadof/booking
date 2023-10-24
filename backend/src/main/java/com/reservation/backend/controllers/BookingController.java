package com.reservation.backend.controllers;

import com.reservation.backend.dto.BookingDTO;
import com.reservation.backend.services.BookingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/bookings")
@CrossOrigin
@Slf4j
@Validated
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @GetMapping("{id}")
    public ResponseEntity<BookingDTO> getBookingById(@PathVariable Long id,
                                                     @RequestHeader("Authorization") String token) {
        BookingDTO foundBooking = this.bookingService.findBookingById(id, token);
        return ResponseEntity.ok().body(foundBooking);
    }


}
