package com.reservation.backend.mocks.dto;

import com.reservation.backend.dto.LocationDTO;

public class LocationDtoMock {

    public static LocationDTO getLocationDtoMock(Long id) {
        return new LocationDTO(id, "Tallinn");
    }

}
