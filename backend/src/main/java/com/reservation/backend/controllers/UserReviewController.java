package com.reservation.backend.controllers;

import com.reservation.backend.dto.ReviewDTO;
import com.reservation.backend.dto.UserReviewDTO;
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

@Tag(name = "UserReview", description = "API operations with Review")

@RestController
@RequestMapping("/api/v1/user-review")
@CrossOrigin
@Slf4j
@Validated
@RequiredArgsConstructor
public class UserReviewController {

    private final UserReviewService userReviewService;
    @Operation(summary = "Add review to Housing")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Review added",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ReviewDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid Review data", content = @Content(mediaType = "*/*")),
            @ApiResponse(responseCode = "403", description = "Access denied", content = @Content(mediaType = "*/*")),
            @ApiResponse(responseCode = "404", description = "Housing not found", content = @Content(mediaType = "*/*")),
    })
    @PostMapping()
    public ResponseEntity<UserReviewDTO> addUserReview(@Valid @RequestBody UserReviewDTO userReviewDTO) {
        log.info("REST request to add user review");
        return ResponseEntity.ok().body(userReviewService.saveReviewUser(userReviewDTO));
    }

}
