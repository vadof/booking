package com.reservation.backend.controllers;

import com.reservation.backend.dto.ReviewDTO;
import com.reservation.backend.dto.UserDTO;
import com.reservation.backend.services.ReviewService;
import com.reservation.backend.services.UserReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Review", description = "API operations with Review")

@RestController
@RequestMapping("/api/v1/reviews")
@CrossOrigin
@Slf4j
@Validated
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;

    @Operation(summary = "Add review to Housing")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Review added",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ReviewDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid Review data", content = @Content(mediaType = "*/*")),
            @ApiResponse(responseCode = "403", description = "Access denied", content = @Content(mediaType = "*/*")),
            @ApiResponse(responseCode = "404", description = "Housing not found", content = @Content(mediaType = "*/*")),
    })



    @PostMapping("/{housingId}")
    public ResponseEntity<ReviewDTO> addReviewNew(@PathVariable Long housingId, @Valid @RequestBody ReviewDTO reviewDTO) {
        log.info("REST request to add review");
        return ResponseEntity.ok().body(reviewService.saveReview(housingId, reviewDTO));
    }

    @Operation(summary = "Get Review by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Review found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ReviewDTO.class))),
            @ApiResponse(responseCode = "403", description = "Access denied", content = @Content(mediaType = "*/*")),
            @ApiResponse(responseCode = "404", description = "Review not found", content = @Content(mediaType = "*/*")),
    })
    @GetMapping("/{id}")
    public ResponseEntity<ReviewDTO> getReviewById(@PathVariable Long id) {
        log.info("REST request to get review {}", id);
        return ResponseEntity.ok().body(reviewService.findReviewById(id));
    }

    @Operation(summary = "Update Review")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Review updated",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ReviewDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid Review data", content = @Content(mediaType = "*/*")),
            @ApiResponse(responseCode = "403", description = "Access denied", content = @Content(mediaType = "*/*")),
            @ApiResponse(responseCode = "404", description = "Review not found", content = @Content(mediaType = "*/*")),
    })
    @PutMapping
    public ResponseEntity<ReviewDTO> updateReview(@RequestBody ReviewDTO reviewDTO) {
        log.info("REST request to update review {}", reviewDTO.getId());
        return ResponseEntity.ok().body(reviewService.updateReview(reviewDTO));
    }

    @Operation(summary = "Delete Review")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Review deleted",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ReviewDTO.class))),
            @ApiResponse(responseCode = "403", description = "Access denied", content = @Content(mediaType = "*/*")),
            @ApiResponse(responseCode = "404", description = "Review not found", content = @Content(mediaType = "*/*")),
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<ReviewDTO> deleteReview(@PathVariable Long id) {
        log.info("REST request to delete review {}", id);
        return ResponseEntity.ok().body(reviewService.deleteReview(id));
    }

}
