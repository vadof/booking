package com.reservation.backend.controllers;

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
    public List<Housing> getAllHousings() {
        return housingService.getAllHousings();
    }

    @PostMapping
    public ResponseEntity<?> addHousing(@RequestBody HousingAddRequest housingForm, @RequestHeader String token) {
        Optional<Housing> res = housingService.addHousing(housingForm, token);
        return ResponseEntity.ok(res);
    }
}
