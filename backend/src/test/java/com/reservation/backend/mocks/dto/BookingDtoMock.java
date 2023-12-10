package com.reservation.backend.mocks.dto;

import com.reservation.backend.dto.BookingDTO;

import java.math.BigDecimal;
import java.time.LocalDate;

public class BookingDtoMock {
    public static BookingDTO getBookingDtoMock(Long id) {
        return new BookingDTO(id, LocalDate.of(2023, 12, 3),
                LocalDate.of(2023, 12, 4), "No info", 1,
                BigDecimal.valueOf(100), UserDtoMock.getUserDtoMock(id, "email" + id + "@gmail.com"),
                HousingPreviewDtoMock.getHousingPreviewDtoMock(id));
    }
}
