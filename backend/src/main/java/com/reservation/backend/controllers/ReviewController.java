package com.reservation.backend.controllers;

import com.reservation.backend.dto.PaginatedResponseDTO;
import com.reservation.backend.dto.ReviewDTO;
import com.reservation.backend.services.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;


@RestController
@RequestMapping("/api/v1/reviews")
@CrossOrigin
@Slf4j
@Validated
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping("/{housingId}")
    public ResponseEntity<ReviewDTO> addReview(@PathVariable Long housingId, @Valid @RequestBody ReviewDTO reviewDTO,
                                               @RequestHeader("Authorization") String token) {
        log.info("REST request to add review");
        ReviewDTO savedReview = this.reviewService.saveReview(housingId, reviewDTO, token);
        return ResponseEntity.ok().body(savedReview);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReviewDTO> getReviewById(@PathVariable Long id) {
        log.info("REST request to get review {}", id);
        ReviewDTO foundReview = this.reviewService.findReviewById(id);
        return ResponseEntity.ok().body(foundReview);
    }

}
