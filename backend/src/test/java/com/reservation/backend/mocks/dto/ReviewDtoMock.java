package com.reservation.backend.mocks.dto;

import com.reservation.backend.dto.ReviewDTO;

import java.time.LocalDate;

public class ReviewDtoMock {

    public static ReviewDTO getReviewDtoMock(Long id) {
        return ReviewDTO.builder()
                .id(id)
                .rating(10)
                .text("Nice place")
                .date(LocalDate.now())
                .build();
    }

}
