package com.reservation.backend.controllers;

import com.reservation.backend.dto.HousingDTO;
import com.reservation.backend.dto.HousingPreviewDTO;
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
