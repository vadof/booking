package com.reservation.backend.controllers;

import com.reservation.backend.dto.*;
import com.reservation.backend.dto.search.HousingSearchDTO;
import com.reservation.backend.requests.HousingAddRequest;
import com.reservation.backend.services.BookingService;
import com.reservation.backend.services.HousingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

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
        PaginatedResponseDTO<HousingPreviewDTO> paginatedResponse = this.housingService.getAllHousings(housingSearchDTO);
        return ResponseEntity.ok().body(paginatedResponse);
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
        Optional<HousingPreviewDTO> optionalHousing = housingService.updateHousing(id, housingAddRequest, token);
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
        Optional<HousingPreviewDTO> optionalHousingDTO = this.housingService.publishHousing(housingId, token, value);
        if (optionalHousingDTO.isPresent()) {
            return ResponseEntity.ok(optionalHousingDTO.get());
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to publish housing");
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<HousingDTO> getHousingById(@PathVariable Long id, @RequestHeader("Authorization") String token) {
        Optional<HousingDTO> optionalHousingDTO = housingService.getHousingById(id, token);

        return optionalHousingDTO.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/my")
    public ResponseEntity<List<HousingDTO>> getHousingByOwner(@RequestHeader("Authorization") String token) {
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
}
