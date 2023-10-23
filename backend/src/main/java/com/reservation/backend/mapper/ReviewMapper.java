package com.reservation.backend.mapper;

import com.reservation.backend.dto.ReviewDTO;
import com.reservation.backend.entities.Review;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = UserMapper.class)
public interface ReviewMapper extends EntityMapper<Review, ReviewDTO> {
}
