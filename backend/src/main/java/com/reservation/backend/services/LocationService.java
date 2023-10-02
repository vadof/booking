package com.reservation.backend.services;

import com.reservation.backend.dto.LocationDTO;
import com.reservation.backend.entities.Location;
import com.reservation.backend.mapper.LocationMapper;
import com.reservation.backend.repositories.LocationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LocationService {

    private final LocationRepository locationRepository;
    private final LocationMapper locationMapper;


    public List<LocationDTO> getAllLocations() {
        List<Location> locationList = locationRepository.findAll();
        return locationMapper.toLocationDTOList(locationList);
    }
}
