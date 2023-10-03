package com.reservation.backend.mapper;

import com.reservation.backend.dto.HousingDTO;
import com.reservation.backend.entities.Housing;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = ImageMapper.class)
public interface HousingMapper {
    HousingDTO toHousingDTO(Housing housing);
    List<HousingDTO> toHousingDTOList(List<Housing> housingList);
}
