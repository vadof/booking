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

    @PutMapping
    public ResponseEntity<ReviewDTO> updateReview(@RequestBody ReviewDTO reviewDTO, String token) {
        log.info("REST request to update review {}", reviewDTO.getId());
        ReviewDTO updatedReview = this.reviewService.updateReview(reviewDTO, token);
        return ResponseEntity.ok().body(updatedReview);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ReviewDTO> deleteReview(@PathVariable Long id, String token) {
        log.info("REST request to delete review {}", id);
        ReviewDTO deletedReview = this.reviewService.deleteReview(id, token);
        return ResponseEntity.ok().body(deletedReview);
    }

}
