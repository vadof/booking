package com.reservation.backend.mocks.entity;

import com.reservation.backend.entities.User;

import java.time.LocalDate;
import java.util.ArrayList;

public class UserMock {

    public static User getUserMock(Long id) {
        return getUserMock(id, "email@gmail.com");
    }

    public static User getUserMock(Long id, String email) {
        return User.builder()
                .id(id)
                .firstname("First")
                .lastname("Last")
                .favourites(new ArrayList<>())
                .registerDate(LocalDate.of(2023, 12, 1))
                .email(email)
                .build();
    }

}
