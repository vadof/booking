package com.reservation.backend.mocks.entity;

import com.reservation.backend.entities.Image;

public class ImageMock {

    public static Image getImageMock(Long id) {
        return Image.builder()
                .id(id)
                .housing(HousingMock.getHousingMock(id))
                .name("bedroom.jpg")
                .contentType("image/jpeg")
                .build();
    }

}
