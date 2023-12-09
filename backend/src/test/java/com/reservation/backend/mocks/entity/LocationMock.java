package com.reservation.backend.mocks.entity;

import com.reservation.backend.entities.Location;

public class LocationMock {

    public static Location getLocationMock(Long id) {
        return Location.builder()
                .id(id)
                .name("Tallinn")
                .build();
    }

}
