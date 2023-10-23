package com.reservation.backend.mapper;

import com.reservation.backend.dto.UserDTO;
import com.reservation.backend.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper extends EntityMapper<User, UserDTO> {
}
