package com.reservation.backend.mapper;

import com.reservation.backend.dto.HousingPreviewDTO;
import com.reservation.backend.entities.Housing;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;


@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = {ImageMapper.class, LocationMapper.class})
public interface HousingPreviewMapper extends EntityMapper<Housing, HousingPreviewDTO> {

}
