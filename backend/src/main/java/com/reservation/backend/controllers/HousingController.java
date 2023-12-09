package com.reservation.backend.controllers;

import com.reservation.backend.dto.*;
import com.reservation.backend.dto.search.HousingSearchDTO;
import com.reservation.backend.services.BookingService;
import com.reservation.backend.services.HousingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Housing", description = "API operations with Housing")
@CrossOrigin
@RestController
@Slf4j
@Validated
@RequiredArgsConstructor
@RequestMapping("/api/v1/housings")
public class HousingController {
    private final HousingService housingService;
    private final BookingService bookingService;

    @Operation(summary = "Get list of Housings as a Paginated Response")
    @ApiResponse(responseCode = "200", description = "Return Paginated Response",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = PaginatedResponseDTO.class)))
    @GetMapping
    public ResponseEntity<PaginatedResponseDTO<HousingPreviewDTO>> getAllHousings(HousingSearchDTO housingSearchDTO) {
        log.info("REST request to get all housings");
        PaginatedResponseDTO<HousingPreviewDTO> paginatedResponse = housingService.getAllHousings(housingSearchDTO);
        return ResponseEntity.ok().body(paginatedResponse);
    }

    @Operation(summary = "Create new Housing")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Housing created",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = HousingDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid Housing data", content = @Content(mediaType = "*/*")),
    })
    @PostMapping
    public ResponseEntity<HousingDTO> addHousing(@RequestBody @Valid HousingDTO housingDTO) {
        log.info("REST request to add housing {}", housingDTO);
        return ResponseEntity.ok().body(housingService.addHousing(housingDTO));
    }

    @Operation(summary = "Update Housing")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Housing updated",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = HousingDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid Housing data", content = @Content(mediaType = "*/*")),
            @ApiResponse(responseCode = "403", description = "Access denied", content = @Content(mediaType = "*/*")),
            @ApiResponse(responseCode = "404", description = "Housing not found", content = @Content(mediaType = "*/*")),
    })
    @PutMapping
    public ResponseEntity<HousingDTO> updateHousing(@RequestBody @Valid HousingDTO housingDTO) {
        log.info("REST request to update housing {}", housingDTO);
        return ResponseEntity.ok().body(housingService.updateHousing(housingDTO));
    }

    @Operation(summary = "Change Housing Preview Image")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Preview Image changed",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ImageDTO.class))),
            @ApiResponse(responseCode = "403", description = "Access denied", content = @Content(mediaType = "*/*")),
            @ApiResponse(responseCode = "404", description = "Housing/Image not found", content = @Content(mediaType = "*/*")),
    })
    @PutMapping("/{housingId}/previewImage/{imageId}")
    public ResponseEntity<ImageDTO> changePreviewImage(@PathVariable Long housingId, @PathVariable Long imageId) {
        log.info("REST request to change Housing#{} preview image", housingId);
        return ResponseEntity.ok().body(housingService.changeImagePreview(housingId, imageId));
    }

    @Operation(summary = "Publish Housing")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Housing published/unpublished",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = HousingPreviewDTO.class))),
            @ApiResponse(responseCode = "403", description = "Access denied", content = @Content(mediaType = "*/*")),
            @ApiResponse(responseCode = "404", description = "Housing not found", content = @Content(mediaType = "*/*")),
    })
    @PutMapping("/publish/{id}")
    public ResponseEntity<HousingPreviewDTO> publishHousing(@PathVariable Long id, @RequestParam Boolean value) {
        log.info("REST request to publish Housing#{}", id);
        return ResponseEntity.ok().body(housingService.publishHousing(id, value));
    }

    @Operation(summary = "Get Housing by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Housing found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = HousingDTO.class))),
            @ApiResponse(responseCode = "403", description = "Access denied", content = @Content(mediaType = "*/*")),
            @ApiResponse(responseCode = "404", description = "Housing not found", content = @Content(mediaType = "*/*")),
    })
    @GetMapping("/{id}")
    public ResponseEntity<HousingDTO> getHousingById(@PathVariable Long id) {
        log.info("REST request to get Housing#{}", id);
        return ResponseEntity.ok().body(housingService.getHousingById(id));
    }

    @Operation(summary = "Get all User's Housings")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Return list of Housings",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = HousingPreviewDTO.class))),
            @ApiResponse(responseCode = "403", description = "Access denied", content = @Content(mediaType = "*/*")),
    })
    @GetMapping("/my")
    public ResponseEntity<List<HousingPreviewDTO>> getHousingByOwner() {
        log.info("REST request to get owner Housings");
        List<HousingPreviewDTO> optionalHousingDTOList = housingService.getHousingsByOwner();
        return ResponseEntity.ok(optionalHousingDTOList);
    }

    @Operation(summary = "Book a Housing")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Housing booked",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = BookingDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid dates", content = @Content(mediaType = "*/*")),
            @ApiResponse(responseCode = "404", description = "Housing not found", content = @Content(mediaType = "*/*")),
    })
    @PostMapping("/{id}/book")
    public ResponseEntity<BookingDTO> bookHousing(@PathVariable Long id, @Valid @RequestBody BookingDTO bookingDTO) {
        log.info("REST request to book housing with id {}", id);
        BookingDTO savedBooking = bookingService.bookHousing(id, bookingDTO);
        return ResponseEntity.ok().body(savedBooking);
    }

    @Operation(summary = "Delete a Housing")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Housing deleted",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = HousingDTO.class))),
            @ApiResponse(responseCode = "403", description = "Access denied", content = @Content(mediaType = "*/*")),
            @ApiResponse(responseCode = "404", description = "Housing not found", content = @Content(mediaType = "*/*")),
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<HousingDTO> deleteHousing(@PathVariable Long id) {
        log.info("REST request to delete housing with id {}", id);
        HousingDTO deletedHousing = housingService.deleteHousing(id);
        return ResponseEntity.ok().body(deletedHousing);
    }

    @Operation(summary = "Get min and max Housing prices")
    @ApiResponse(responseCode = "200", description = "Return Prices",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = PriceDto.class)))
    @GetMapping("/prices")
    public ResponseEntity<PriceDto> getHousingPrices() {
        log.info("REST request to get housing prices");
        PriceDto priceDto = housingService.getHousingPrices();
        return ResponseEntity.ok().body(priceDto);
    }
}
