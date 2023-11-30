package com.reservation.backend.controllers;

import com.reservation.backend.dto.HousingDTO;
import com.reservation.backend.services.HousingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/favourites")
public class UserController {
    private final HousingService housingService;

    @PostMapping("/{housingId}")
    public ResponseEntity<HousingDTO> addToFavourites(@PathVariable Long housingId) {
        log.info("REST request to add Housing#{} to favourites", housingId);
        return ResponseEntity.ok(housingService.addHousingToFavourites(housingId));
    }

    @GetMapping
    public ResponseEntity<List<HousingDTO>> getAllFavourites() {
        log.info("REST request to get all favourites housings");
        List<HousingDTO> favourites = this.housingService.getAllFavourites();
        return ResponseEntity.ok().body(favourites);
    }
}
