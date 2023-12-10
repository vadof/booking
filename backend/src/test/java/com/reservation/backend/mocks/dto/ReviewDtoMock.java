package com.reservation.backend.mocks.dto;

import com.reservation.backend.dto.ReviewDTO;

import java.time.LocalDate;

public class ReviewDtoMock {

    public static ReviewDTO getReviewDtoMock(Long id) {
        return new ReviewDTO(id, "Nice place", 10,
                LocalDate.of(2023, 12, 1),
                UserDtoMock.getUserDtoMock(id, "email" + id + "@gmail.com"));
    }

}
