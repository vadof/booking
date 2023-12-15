package com.reservation.backend.mocks.dto;

import com.reservation.backend.dto.BookingDTO;

import java.math.BigDecimal;
import java.time.LocalDate;

public class BookingDtoMock {
    public static BookingDTO getBookingDtoMock(Long id) {
        return BookingDTO.builder()
                .id(id)
                .additionalInfo("No info")
                .nights(1)
                .checkInDate(LocalDate.of(LocalDate.now().getYear() + 1, 12, 3))
                .checkOutDate(LocalDate.of(LocalDate.now().getYear() + 1, 12, 4))
                .tenant(UserDtoMock.getUserDtoMock(id, "email" + id + "@gmail.com"))
                .totalPrice(BigDecimal.valueOf(100))
                .housing(HousingPreviewDtoMock.getHousingPreviewDtoMock(id))
                .build();
    }
}
