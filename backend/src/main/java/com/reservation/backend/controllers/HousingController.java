package com.reservation.backend.controllers;

import com.reservation.backend.dto.HousingDTO;
import com.reservation.backend.dto.ImageDTO;
import com.reservation.backend.requests.HousingAddRequest;
import com.reservation.backend.services.HousingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
        Optional<HousingDTO> res = housingService.addHousing(housingForm, token);
        if (res.isPresent()) {
            return ResponseEntity.ok(res.get());
        } else {
            return ResponseEntity.badRequest().body("Failed to add housing");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateHousing(@PathVariable Long id, @RequestBody HousingAddRequest housingAddRequest,
                                                  @RequestHeader("Authorization") String token) {
        Optional<HousingDTO> optionalHousing = housingService.updateHousing(id, housingAddRequest, token);
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
        Optional<HousingDTO> optionalHousingDTO = this.housingService.publishHousing(housingId, token, value);
        if (optionalHousingDTO.isPresent()) {
            return ResponseEntity.ok(optionalHousingDTO.get());
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to publish housing");
        }
    }
}
