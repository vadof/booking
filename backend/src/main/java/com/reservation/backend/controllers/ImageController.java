package com.reservation.backend.controllers;

import com.reservation.backend.dto.ImageDTO;
import com.reservation.backend.services.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;


@RestController
@RequestMapping("/api/v1/images")
@RequiredArgsConstructor
public class ImageController {

    private final ImageService imageService;

    @PostMapping("/{housingId}/upload")
    public ResponseEntity<?> uploadImage(@RequestParam("imageFile") MultipartFile imageFile, @PathVariable Long housingId,
                                         @RequestHeader("Authorization") String token) {
        Optional<ImageDTO> optionalImage = this.imageService.addImageToHousing(imageFile, housingId, token);
        if (optionalImage.isPresent()) {
            return ResponseEntity.ok(optionalImage.get());
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to upload image");
        }
    }

    @GetMapping("/{id}")
    public byte[] getImage(@PathVariable Long id) {
        return this.imageService.getImage(id);
    }

}
