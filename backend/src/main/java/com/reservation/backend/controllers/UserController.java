package com.reservation.backend.controllers;

import com.reservation.backend.dto.*;
import com.reservation.backend.dto.search.HousingSearchDTO;
import com.reservation.backend.requests.HousingAddRequest;
import com.reservation.backend.services.BookingService;
import com.reservation.backend.services.HousingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
@Slf4j
@Validated
@RequiredArgsConstructor
@RequestMapping("/api/v1/favourites")
public class UserController {
    private final HousingService housingService;
    private final BookingService bookingService;


    @GetMapping
    public ResponseEntity<List<HousingDTO>> getAllFavourites(@RequestHeader("Authorization") String token) {
        List<HousingDTO> favourites = this.housingService.getAllFavourites(token);
        return ResponseEntity.ok().body(favourites);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateHousing(@PathVariable Long id, @RequestBody HousingAddRequest housingAddRequest,
                                           @RequestHeader("Authorization") String token) {
        Optional<HousingPreviewDTO> optionalHousing = housingService.updateHousing(id, housingAddRequest, token);
        if (optionalHousing.isPresent()) {
            return ResponseEntity.ok(optionalHousing.get());
        } else {
            return ResponseEntity.badRequest().body("Failed to update housing");
        }
    }

    @PutMapping("/{housingId}/previewImage/{imageId}")
    public ResponseEntity<?> changePreviewImage(@PathVariable Long housingId, @PathVariable Long imageId,
                                                @RequestHeader("Authorization") String token) {
        Optional<ImageDTO> optionalImageDTO = this.housingService.changeImagePreview(housingId, imageId, token);
        if (optionalImageDTO.isPresent()) {
            return ResponseEntity.ok(optionalImageDTO.get());
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to change preview image");
        }
    }

    @PutMapping("/publish/{housingId}")
    public ResponseEntity<?> publishHousing(@PathVariable Long housingId, @RequestHeader("Authorization") String token, @RequestParam boolean value) {
        Optional<HousingPreviewDTO> optionalHousingDTO = this.housingService.publishHousing(housingId, token, value);
        if (optionalHousingDTO.isPresent()) {
            return ResponseEntity.ok(optionalHousingDTO.get());
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to publish housing");
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<HousingDTO> getHousingById(@PathVariable Long id, @RequestHeader("Authorization") String token) {
        Optional<HousingDTO> optionalHousingDTO = housingService.getHousingById(id, token);

        return optionalHousingDTO.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HousingDTO> deleteHousing(@PathVariable Long id, @RequestHeader("Authorization") String token) {
        log.info("REST request to delete housing with id {}", id);
        HousingDTO deletedHousing = housingService.deleteHousing(id, token);
        return ResponseEntity.ok().body(deletedHousing);
    }
}
