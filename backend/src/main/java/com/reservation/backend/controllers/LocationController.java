package com.reservation.backend.controllers;

import com.reservation.backend.dto.LocationDTO;
import com.reservation.backend.services.LocationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Location", description = "API operations with Location")
@RestController
@RequestMapping("/api/v1/locations")
public class LocationController {
    private final LocationService locationService;

    public LocationController(LocationService locationService) {
        this.locationService = locationService;
    }

    @Operation(summary = "Get list of Locations")
    @ApiResponse(responseCode = "200", description = "Return Locations",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = LocationDTO.class)))
    @GetMapping
    public List<LocationDTO> getAllLocations() {
        return locationService.getAllLocations();
    }

}
