package com.reservation.backend.mocks.dto;

import com.reservation.backend.dto.UserDTO;

public class UserDtoMock {

    public static UserDTO getUserDtoMock(Long id) {
        return getUserDtoMock(id, "email@gmail.com");
    }

    public static UserDTO getUserDtoMock(Long id, String email) {
        return new UserDTO(id, "First", "Last", email);
    }

}
