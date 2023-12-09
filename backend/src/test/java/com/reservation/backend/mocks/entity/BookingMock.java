package com.reservation.backend.mocks.entity;

import com.reservation.backend.entities.Booking;

import java.math.BigDecimal;
import java.time.LocalDate;

public class BookingMock {

    public static Booking getBookingMock(Long id) {
        return Booking.builder()
                .id(id)
                .nights(1)
                .additionalInfo("No info")
                .totalPrice(BigDecimal.valueOf(100))
                .housing(HousingMock.getHousingMock(id))
                .tenant(UserMock.getUserMock(id, "email" + id + "@gmail.com"))
                .checkInDate(LocalDate.of(2023, 12, 3))
                .checkOutDate(LocalDate.of(2023, 12, 4))
                .build();
    }

}
