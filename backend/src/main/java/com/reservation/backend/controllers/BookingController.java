package com.reservation.backend.controllers;

import com.reservation.backend.dto.BookingDTO;
import com.reservation.backend.dto.PaginatedResponseDTO;
import com.reservation.backend.dto.search.BookingSearchDTO;
import com.reservation.backend.services.BookingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Booking", description = "API operations with Booking")
@RestController
@RequestMapping("/api/v1/bookings")
@CrossOrigin
@Slf4j
@Validated
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @Operation(summary = "Get Booking by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Booking found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = BookingDTO.class))),
            @ApiResponse(responseCode = "403", description = "Access denied", content = @Content(mediaType = "*/*")),
            @ApiResponse(responseCode = "404", description = "Booking not found", content = @Content(mediaType = "*/*")),
    })
    @GetMapping("/{id}")
    public ResponseEntity<BookingDTO> getBookingById(@PathVariable Long id) {
        log.info("REST request to get Booking {}", id);
        BookingDTO foundBooking = bookingService.findBookingById(id);
        return ResponseEntity.ok().body(foundBooking);
    }

    @Operation(summary = "Cancel Booking")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Booking canceled",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = BookingDTO.class))),
            @ApiResponse(responseCode = "403", description = "Access denied", content = @Content(mediaType = "*/*")),
            @ApiResponse(responseCode = "404", description = "Booking not found", content = @Content(mediaType = "*/*")),
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<BookingDTO> cancelBooking(@PathVariable Long id) {
        log.info("REST request to cancel Booking {}", id);
        BookingDTO deletedBooking = bookingService.deleteBooking(id);
        return ResponseEntity.ok().body(deletedBooking);
    }

    @Operation(summary = "Get list of User's Bookings as a Paginated Response")
    @ApiResponse(responseCode = "200", description = "Return Paginated Response",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = PaginatedResponseDTO.class)))
    @GetMapping
    public ResponseEntity<PaginatedResponseDTO<BookingDTO>> getAllUserBookings(
            BookingSearchDTO bookingSearchDTO) {
        log.info("REST request to get all user Bookings");
        PaginatedResponseDTO<BookingDTO> paginatedResponse = bookingService.findAllUserBookings(bookingSearchDTO);
        return ResponseEntity.ok().body(paginatedResponse);
    }


}
