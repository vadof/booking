package com.reservation.backend.mocks.dto;

import com.reservation.backend.dto.HousingDTO;
import com.reservation.backend.entities.Housing;
import com.reservation.backend.mocks.entity.LocationMock;
import com.reservation.backend.mocks.entity.UserMock;

import java.math.BigDecimal;
import java.sql.Time;
import java.time.LocalTime;
import java.util.ArrayList;

public class HousingDtoMock {

    public static HousingDTO getHousingDtoMock(Long id) {
        return HousingDTO.builder()
                .id(id)
                .location(LocationDtoMock.getLocationDtoMock(id))
                .name("Hotel")
                .published(false)
                .people(2)
                .rooms(2)
                .coordinates("52.5200,13.4050")
                .checkIn(Time.valueOf(LocalTime.of(12, 0, 0)))
                .checkOut(Time.valueOf(LocalTime.of(14, 0, 0)))
                .m2(37)
                .owner(UserDtoMock.getUserDtoMock(id))
                .pricePerNight(BigDecimal.valueOf(100))
                .previewImage(null)
                .images(new ArrayList<>())
                .bookings(new ArrayList<>())
                .reviews(new ArrayList<>())
                .minRentalAge(18)
                .description("Nice hotel")
                .minNights(1)
                .rating(null)
                .build();
    }

}
