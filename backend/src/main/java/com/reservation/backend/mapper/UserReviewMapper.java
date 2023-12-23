package com.reservation.backend.mapper;

import com.reservation.backend.dto.UserReviewDTO;
import com.reservation.backend.entities.UserReview;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = UserMapper.class)
public interface UserReviewMapper extends EntityMapper<UserReview, UserReviewDTO> {

}
