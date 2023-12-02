package com.reservation.backend.controllers;

import com.reservation.backend.dto.HousingDTO;
import com.reservation.backend.dto.HousingPreviewDTO;
import com.reservation.backend.services.HousingService;
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
    @PostMapping("/{housingId}")
    public ResponseEntity<?> addToFavourites(@PathVariable Long housingId, @RequestHeader("Authorization") String token) {
        Optional<HousingDTO> res = housingService.addHousingToFavourites(token, housingId);
        if (res.isPresent()) {
            return ResponseEntity.ok(res.get());
        } else {
            return ResponseEntity.badRequest().body("Failed to add housing");
        }
    }


    @GetMapping
    public ResponseEntity<List<HousingPreviewDTO>> getAllFavourites() {
        log.info("REST request to get user's favourites");
        List<HousingPreviewDTO> favourites = housingService.getAllFavourites();
        return ResponseEntity.ok().body(favourites);
    }

    @PostMapping("/{id}")
    public ResponseEntity<HousingDTO> addToFavourites(@PathVariable Long id) {
        log.info("REST request to get add Housing#{} to favourites", id);
        return ResponseEntity.ok().body(housingService.addHousingToFavourites(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HousingDTO> deleteHousingFromFavourites(@PathVariable Long id) {
        log.info("REST request to delete housing with id {} from user's favourites", id);
        HousingDTO deletedFromFavourites = housingService.deleteFromFavourites(id);
        return ResponseEntity.ok().body(deletedFromFavourites);
    }
}
