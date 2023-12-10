package com.reservation.backend.mocks.dto;

import com.reservation.backend.dto.BookingDTO;

import java.time.LocalDate;

public class BookingDtoMock {
    public static BookingDTO getBookingDtoMock(Long id) {
        return BookingDTO.builder()
                .id(id)
                .checkInDate(LocalDate.of(LocalDate.now().getYear() + 1, 12, 3))
                .checkOutDate(LocalDate.of(LocalDate.now().getYear() + 1, 12, 4))
                .additionalInfo("No info")
                .build();
    }
}
