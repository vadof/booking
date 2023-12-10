package com.reservation.backend.mapper;

import com.reservation.backend.dto.HousingDTO;
import com.reservation.backend.entities.Housing;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = {ImageMapper.class, LocationMapper.class, ReviewMapper.class, UserMapper.class, BookingMapper.class})
public interface HousingMapper extends EntityMapper<Housing, HousingDTO> {
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void update(@MappingTarget Housing entity, HousingDTO dto);
}