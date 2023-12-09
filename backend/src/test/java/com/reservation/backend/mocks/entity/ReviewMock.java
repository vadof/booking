package com.reservation.backend.mocks.entity;

import com.reservation.backend.entities.Review;

import java.time.LocalDate;

public class ReviewMock {

    public static Review getReviewMock(Long id) {
        return Review.builder()
                .id(id)
                .date(LocalDate.of(2023, 12, 1))
                .rating(10)
                .text("Nice place")
                .housing(HousingMock.getHousingMock(id))
                .reviewer(UserMock.getUserMock(id, "email" + id + "@gmail.com"))
                .build();
    }

}
