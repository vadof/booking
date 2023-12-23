package com.reservation.backend.controllers;

import com.reservation.backend.dto.CountryDTO;
import com.reservation.backend.dto.PaginatedResponseDTO;
import com.reservation.backend.dto.search.CountrySearchDTO;
import com.reservation.backend.services.CountryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Country", description = "API operations with Country")
@RestController
@RequestMapping("/api/v1/countries")
public class CountryController {
    private final CountryService countryService;

    public CountryController(CountryService countryService) {
        this.countryService = countryService;
    }
//
//    @Operation(summary = "Get list of Countries")
//    @ApiResponse(responseCode = "200", description = "Return Countries",
//            content = @Content(mediaType = "application/json", schema = @Schema(implementation = CountryDTO.class)))
//    @GetMapping
//    public List<CountryDTO> getAllCountries() {
//        return countryService.getAllCountry();
//    }

    @Operation(summary = "Get list of Housings as a Paginated Response")
    @ApiResponse(responseCode = "200", description = "Return Paginated Response",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = PaginatedResponseDTO.class)))
    @GetMapping
    public ResponseEntity<PaginatedResponseDTO<CountryDTO>> getAllHousings(String nimi, String kood) {
        PaginatedResponseDTO<CountryDTO> paginatedResponse = countryService.getAllCountries(countrySearchDTO);
        return ResponseEntity.ok().body(paginatedResponse);
    }

    //"/api/v1/countries//search"
}
