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

    @GetMapping
    public ResponseEntity<List<HousingDTO>> getAllFavourites(@RequestHeader("Authorization") String token) {
        log.info("REST request to get user's favourites");
        List<HousingDTO> favourites = this.housingService.getAllFavourites(token);
        return ResponseEntity.ok().body(favourites);
    }

    @PostMapping("/{id}")
    public ResponseEntity<?> addToFavourites(@PathVariable Long id, @RequestHeader("Authorization") String token) {
        Optional<HousingDTO> res = housingService.addHousingToFavourites(token, id);
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
