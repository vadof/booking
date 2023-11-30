package com.reservation.backend.controllers;

import com.reservation.backend.dto.*;
import com.reservation.backend.dto.search.HousingSearchDTO;
import com.reservation.backend.requests.HousingAddRequest;
import com.reservation.backend.services.BookingService;
import com.reservation.backend.services.HousingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    @GetMapping
    public ResponseEntity<List<HousingDTO>> getAllFavourites(@RequestHeader("Authorization") String token) {
        log.info("REST request to get user's favourites");
        List<HousingDTO> favourites = this.housingService.getAllFavourites(token);
        return ResponseEntity.ok().body(favourites);
    }

    @PostMapping("/{id}")
    public ResponseEntity<?> addToFavourites(@PathVariable Long Id, @RequestHeader("Authorization") String token) {
        Optional<HousingDTO> res = housingService.addHousingToFavourites(token, Id);
        if (res.isPresent()) {
            return ResponseEntity.ok(res.get());
        } else {
            return ResponseEntity.badRequest().body("Failed to add housing");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HousingDTO> deleteHousingFromFavourites(@PathVariable Long id, @RequestHeader("Authorization") String token) {
        log.info("REST request to delete housing with id {} from user's favourites", id);
        HousingDTO deletedFromFavourites = housingService.deleteFromFavourites(id, token);
        return ResponseEntity.ok().body(deletedFromFavourites);
    }
}
