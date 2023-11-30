package com.reservation.backend.controllers;

import com.reservation.backend.dto.*;
import com.reservation.backend.dto.search.HousingSearchDTO;
import com.reservation.backend.requests.HousingAddRequest;
import com.reservation.backend.services.BookingService;
import com.reservation.backend.services.HousingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@Slf4j
@Validated
@RequiredArgsConstructor
@RequestMapping("/api/v1/housings")
public class HousingController {
    private final HousingService housingService;
    private final BookingService bookingService;

    @GetMapping
    public ResponseEntity<PaginatedResponseDTO<HousingPreviewDTO>> getAllHousings(HousingSearchDTO housingSearchDTO) {
        log.info("REST request to get all housings");
        PaginatedResponseDTO<HousingPreviewDTO> paginatedResponse = this.housingService.getAllHousings(housingSearchDTO);
        return ResponseEntity.ok().body(paginatedResponse);
    }

    @PostMapping
    public ResponseEntity<HousingDTO> addHousing(@RequestBody HousingAddRequest housingForm, @RequestHeader("Authorization") String token) {
        log.info("REST request to add housing {}", housingForm);
        return ResponseEntity.ok().body(housingService.addHousing(housingForm, token));
    }

    @PutMapping("/{id}")
    public ResponseEntity<HousingPreviewDTO> updateHousing(@PathVariable Long id, @RequestBody HousingAddRequest housingAddRequest,
                                                           @RequestHeader("Authorization") String token) {
        log.info("REST request to update housing {}", housingAddRequest);
        return ResponseEntity.ok().body(housingService.updateHousing(id, housingAddRequest, token));
    }

    @PutMapping("/{housingId}/previewImage/{imageId}")
    public ResponseEntity<ImageDTO> changePreviewImage(@PathVariable Long housingId, @PathVariable Long imageId,
                                                       @RequestHeader("Authorization") String token) {
        log.info("REST request to change Housing#{} preview image", housingId);
        return ResponseEntity.ok().body(housingService.changeImagePreview(housingId, imageId, token));
    }

    @PutMapping("/publish/{housingId}")
    public ResponseEntity<HousingPreviewDTO> publishHousing(@PathVariable Long housingId, @RequestHeader("Authorization") String token, @RequestParam boolean value) {
        log.info("REST request to publish Housing#{}", housingId);
        return ResponseEntity.ok().body(housingService.publishHousing(housingId, token, value));
    }

    @GetMapping("/{id}")
    public ResponseEntity<HousingDTO> getHousingById(@PathVariable Long id, @RequestHeader("Authorization") String token) {
        log.info("REST request to get Housing#{}", id);
        return ResponseEntity.ok().body(housingService.getHousingById(id, token));
    }

    @GetMapping("/my")
    public ResponseEntity<List<HousingDTO>> getHousingByOwner(@RequestHeader("Authorization") String token) {
        log.info("REST request to get owner Housings");
        List<HousingDTO> optionalHousingDTOList = housingService.getHousingsByOwner(token);
        return ResponseEntity.ok(optionalHousingDTOList);
    }

    @PostMapping("/{housingId}/book")
    public ResponseEntity<BookingDTO> bookHousing(@PathVariable Long housingId, @Valid @RequestBody BookingDTO bookingDTO,
                                                  @RequestHeader("Authorization") String token) {
        log.info("REST request to book housing with id {}", housingId);
        BookingDTO savedBooking = this.bookingService.bookHousing(housingId, bookingDTO, token);
        return ResponseEntity.ok().body(savedBooking);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HousingDTO> deleteHousing(@PathVariable Long id, @RequestHeader("Authorization") String token) {
        log.info("REST request to delete housing with id {}", id);
        HousingDTO deletedHousing = housingService.deleteHousing(id, token);
        return ResponseEntity.ok().body(deletedHousing);
    }

    @GetMapping("/prices")
    public ResponseEntity<PriceDto> getHousingPrices() {
        log.info("REST request to get housing prices");
        PriceDto priceDto = housingService.getHousingPrices();
        return ResponseEntity.ok().body(priceDto);
    }
}
