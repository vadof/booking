package com.reservation.backend.controllers;

import com.reservation.backend.dto.HousingDTO;
import com.reservation.backend.entities.Housing;
import com.reservation.backend.requests.HousingAddRequest;
import com.reservation.backend.services.HousingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/housings")
public class HousingController {
    private final HousingService housingService;

    @Autowired
    public HousingController(HousingService housingService) {
        this.housingService = housingService;
    }

    @GetMapping
    public List<HousingDTO> getAllHousings(@RequestParam(required = false) String locationName,
                                           @RequestParam(required = false, defaultValue = "0") int minPrice,
                                           @RequestParam(required = false, defaultValue = "0") int maxPrice,
                                           @RequestParam(required = false, defaultValue = "0") int amountPeople) {
        return housingService.getAllHousings(locationName, minPrice, maxPrice, amountPeople);
    }

    @PostMapping
    public ResponseEntity<?> addHousing(@RequestBody HousingAddRequest housingForm, @RequestHeader("Authorization") String token) {
        Optional<Housing> res = housingService.addHousing(housingForm, token);
        if (res.isPresent()) {
            return ResponseEntity.ok(res.get());
        } else {
            return ResponseEntity.badRequest().body("Failed to add housing");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateHousing(@PathVariable Long id, @RequestBody HousingAddRequest housingAddRequest,
                                                  @RequestHeader("Authorization") String token) {
        Optional<Housing> optionalHousing = housingService.updateHousing(id, housingAddRequest, token);
        if (optionalHousing.isPresent()) {
            return ResponseEntity.ok(optionalHousing.get());
        } else {
            return ResponseEntity.badRequest().body("Failed to update housing");
        }
    }
}
