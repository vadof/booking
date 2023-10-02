package com.reservation.backend.mapper;

import com.reservation.backend.dto.HousingDTO;
import com.reservation.backend.dto.LocationDTO;
import com.reservation.backend.entities.Housing;
import com.reservation.backend.entities.Location;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface LocationMapper {
    LocationDTO toLocationDTO(Location location);

    List<LocationDTO> toLocationDTOList(List<Location> locationList);
}
