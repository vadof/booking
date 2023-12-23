package com.reservation.backend.mapper;

import com.reservation.backend.dto.CountryDTO;
import com.reservation.backend.entities.Country;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.Optional;
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CountryMapper extends EntityMapper<Country, CountryDTO>{

}
