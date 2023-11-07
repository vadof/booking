package com.reservation.backend.services;

import com.reservation.backend.dto.ReviewDTO;
import com.reservation.backend.entities.Housing;
import com.reservation.backend.entities.Review;
import com.reservation.backend.entities.User;
import com.reservation.backend.exceptions.AppException;
import com.reservation.backend.mapper.ReviewMapper;
import com.reservation.backend.repositories.HousingRepository;
import com.reservation.backend.repositories.ReviewRepository;
import com.reservation.backend.security.JwtService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ReviewMapper reviewMapper;
    private final JwtService jwtService;
    private final HousingRepository housingRepository;

    @Transactional
    public ReviewDTO saveReview(Long housingId, ReviewDTO reviewDTO, String token) {
        Housing housing = this.housingRepository.findByIdAndPublishedTrue(housingId).orElseThrow(
                () -> new AppException(String.format("Housing with id %s not found", housingId), HttpStatus.NOT_FOUND));
        User reviewer = jwtService.getUserFromBearerToken(token).orElseThrow();

        if (housing.getOwner().equals(reviewer)) {
            throw new AppException("The owner cannot leave reviews", HttpStatus.FORBIDDEN);
        }

        if (this.userAlreadyLeftReview(housing, reviewer)) {
            throw new AppException("Review has already been left", HttpStatus.FORBIDDEN);
        }

        Review review = this.reviewMapper.toEntity(reviewDTO);
        review.setHousing(housing);
        review.setReviewer(reviewer);

        this.reviewRepository.save(review);
        this.updateHousingRating(housing);
        return this.reviewMapper.toDto(review);
    }

    public ReviewDTO findReviewById(Long id) {
        Review review = this.getReviewById(id);
        return this.reviewMapper.toDto(review);
    }

    @Transactional
    public ReviewDTO updateReview(ReviewDTO reviewDTO, String token) {
        Review review = this.getReviewById(reviewDTO.getId());
        User reviewer = this.jwtService.getUserFromBearerToken(token).orElseThrow();

        if (!reviewer.equals(review.getReviewer())) {
            throw new AppException("Access to update review denied", HttpStatus.FORBIDDEN);
        }

        review.setText(review.getText());
        review.setRating(review.getRating());
        this.reviewRepository.save(review);

        this.updateHousingRating(review.getHousing());

        return this.reviewMapper.toDto(review);
    }

    @Transactional
    public ReviewDTO deleteReview(Long id, String token) {
        Review review = this.getReviewById(id);
        User reviewer = this.jwtService.getUserFromBearerToken(token).orElseThrow();

        if (!review.getReviewer().equals(reviewer)) {
            throw new AppException("Access to delete review denied", HttpStatus.FORBIDDEN);
        }

        Housing housing = review.getHousing();
        this.reviewRepository.delete(review);
        this.updateHousingRating(housing);
        return this.reviewMapper.toDto(review);
    }

    private void updateHousingRating(Housing housing) {
        Long ratingFromAllReviews = 0L;
        if (housing.getReviews().isEmpty()) {
            housing.setRating(null);
        } else {
            for (Review review : housing.getReviews()) {
                ratingFromAllReviews += review.getRating();
            }
            housing.setRating(new BigDecimal(ratingFromAllReviews / housing.getReviews().size()));
        }
        this.housingRepository.save(housing);
    }

    private boolean userAlreadyLeftReview(Housing housing, User user) {
        return housing.getReviews().stream().anyMatch(r -> r.getReviewer().equals(user));
    }

    private Review getReviewById(Long id) {
        return this.reviewRepository.findById(id).orElseThrow(
                () -> new AppException(String.format("Review with id %s not found", id), HttpStatus.NOT_FOUND));
    }
}
