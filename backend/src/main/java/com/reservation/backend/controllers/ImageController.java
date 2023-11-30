package com.reservation.backend.controllers;

import com.reservation.backend.dto.ImageDTO;
import com.reservation.backend.services.ImageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@RestController
@Slf4j
@RequestMapping("/api/v1/images")
@RequiredArgsConstructor
public class ImageController {

    private final ImageService imageService;

    @PostMapping("/{housingId}/upload")
    public ResponseEntity<ImageDTO> uploadImage(@RequestParam("imageFile") MultipartFile imageFile, @PathVariable Long housingId,
                                                @RequestHeader("Authorization") String token) {
        log.info("REST request to upload image to Housing#{}", housingId);
        return ResponseEntity.ok().body(imageService.addImageToHousing(imageFile, housingId, token));
    }

    @GetMapping("/{id}")
    public byte[] getImage(@PathVariable Long id) {
        log.info("REST request to get Image#{}", id);
        return imageService.getImage(id);
    }

}
