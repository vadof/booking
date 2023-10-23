package com.reservation.backend.mapper;

import com.reservation.backend.dto.HousingDTO;
import com.reservation.backend.entities.Housing;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = {ImageMapper.class, LocationMapper.class, ReviewMapper.class, UserMapper.class, BookingMapper.class})
public interface HousingMapper extends EntityMapper<Housing, HousingDTO> {

}