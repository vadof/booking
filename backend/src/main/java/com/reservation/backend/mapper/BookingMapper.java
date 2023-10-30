package com.reservation.backend.mapper;

import com.reservation.backend.dto.BookingDTO;
import com.reservation.backend.entities.Booking;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = {UserMapper.class, HousingPreviewMapper.class})
public interface BookingMapper extends EntityMapper<Booking, BookingDTO> {
}

