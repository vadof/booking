package com.reservation.backend.mapper;

import com.reservation.backend.dto.LocationDTO;
import com.reservation.backend.entities.Location;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface LocationMapper extends EntityMapper<Location, LocationDTO> {

}
