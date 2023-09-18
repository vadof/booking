package com.reservation.backend.controllers;

import com.reservation.backend.entities.Housing;
import com.reservation.backend.services.HousingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.xml.stream.Location;
import java.util.List;

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
    public List<Housing> getAllHousings(@RequestParam(required = false) String locationName,
                                        @RequestParam(required = false) int minPrice,
                                        @RequestParam(required = false) int maxPrice,
                                        @RequestParam(required = false) int amountPeople) {
        return housingService.getAllHousings(locationName, minPrice, maxPrice, amountPeople);
    }

}