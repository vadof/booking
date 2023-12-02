package com.reservation.backend.controllers;

import com.reservation.backend.dto.ReviewDTO;
import com.reservation.backend.services.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/reviews")
@CrossOrigin
@Slf4j
@Validated
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping("/{housingId}")
    public ResponseEntity<ReviewDTO> addReview(@PathVariable Long housingId, @Valid @RequestBody ReviewDTO reviewDTO) {
        log.info("REST request to add review");
        return ResponseEntity.ok().body(reviewService.saveReview(housingId, reviewDTO));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReviewDTO> getReviewById(@PathVariable Long id) {
        log.info("REST request to get review {}", id);
        return ResponseEntity.ok().body(reviewService.findReviewById(id));
    }

    @PutMapping
    public ResponseEntity<ReviewDTO> updateReview(@RequestBody ReviewDTO reviewDTO) {
        log.info("REST request to update review {}", reviewDTO.getId());
        return ResponseEntity.ok().body(reviewService.updateReview(reviewDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ReviewDTO> deleteReview(@PathVariable Long id) {
        log.info("REST request to delete review {}", id);
        return ResponseEntity.ok().body(reviewService.deleteReview(id));
    }

}
