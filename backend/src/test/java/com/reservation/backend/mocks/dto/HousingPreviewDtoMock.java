package com.reservation.backend.mocks.dto;

import com.reservation.backend.dto.HousingPreviewDTO;

import java.math.BigDecimal;

public class HousingPreviewDtoMock {

    public static HousingPreviewDTO getHousingPreviewDtoMock(Long id) {
        return HousingPreviewDTO.builder()
                .id(id)
                .location(LocationDtoMock.getLocationDtoMock(id))
                .name("Hotel")
                .published(false)
                .people(2)
                .coordinates("52.5200,13.4050")
                .pricePerNight(BigDecimal.valueOf(100))
                .previewImage(null)
                .rating(null)
                .build();
    }

}
