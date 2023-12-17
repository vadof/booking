package com.reservation.backend.controllers;

import com.reservation.backend.dto.HousingDTO;
import com.reservation.backend.dto.HousingPreviewDTO;
import com.reservation.backend.dto.PaginatedResponseDTO;
import com.reservation.backend.dto.search.HousingSearchDTO;
import com.reservation.backend.services.HousingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Favourite Housings", description = "API operations with favourite Housings")
@CrossOrigin
@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/favourites")
public class UserController {
    private final HousingService housingService;

    //@Operation(summary = "Get list of favourite Housings")
    //@ApiResponse(responseCode = "200", description = "Return List of Housings",
    //        content = @Content(mediaType = "application/json", schema = @Schema(implementation = HousingPreviewDTO.class)))
    //@GetMapping
    //public ResponseEntity<List<HousingPreviewDTO>> getAllFavourites() {
    //    log.info("REST request to get user's favourites");
    //    List<HousingPreviewDTO> favourites = housingService.getAllFavourites();
    //    return ResponseEntity.ok().body(favourites);
    //}

    @Operation(summary = "Get list of favourite Housings as paginated response")
    @ApiResponse(responseCode = "200", description = "Return List of Housings as paginated response",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = HousingPreviewDTO.class)))
    @GetMapping
    public ResponseEntity<PaginatedResponseDTO<HousingPreviewDTO>> getAllFavourites(HousingSearchDTO housingSearchDTO) {
        log.info("REST request to get user's favourites");
        PaginatedResponseDTO<HousingPreviewDTO> favourites = housingService.getAllFavourites(housingSearchDTO);
        return ResponseEntity.ok().body(favourites);
    }

    @Operation(summary = "Add Housing to favourites")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Housing added to favourites",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = HousingDTO.class))),
            @ApiResponse(responseCode = "404", description = "Housing not found", content = @Content(mediaType = "*/*")),
    })
    @PostMapping("/{id}")
    public ResponseEntity<HousingDTO> addToFavourites(@PathVariable Long id) {
        log.info("REST request to add Housing#{} to favourites", id);
        return ResponseEntity.ok().body(housingService.addHousingToFavourites(id));
    }

    @Operation(summary = "Delete Housing from favourites")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Housing deleted from favourites",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = HousingDTO.class))),
            @ApiResponse(responseCode = "404", description = "Housing not found", content = @Content(mediaType = "*/*")),
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<HousingDTO> deleteHousingFromFavourites(@PathVariable Long id) {
        log.info("REST request to delete Housing#{} from user's favourites", id);
        HousingDTO deletedFromFavourites = housingService.deleteFromFavourites(id);
        return ResponseEntity.ok().body(deletedFromFavourites);
    }
}
