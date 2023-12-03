package com.reservation.backend.controllers;

import com.reservation.backend.dto.ImageDTO;
import com.reservation.backend.services.ImageService;
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
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "Image", description = "API operations with Images")
@RestController
@Slf4j
@RequestMapping("/api/v1/images")
@RequiredArgsConstructor
public class ImageController {

    private final ImageService imageService;

    @Operation(summary = "Add Image to Housing")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Image added",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ImageDTO.class))),
            @ApiResponse(responseCode = "403", description = "Access denied", content = @Content(mediaType = "*/*")),
    })
    @PostMapping("/{housingId}/upload")
    public ResponseEntity<ImageDTO> uploadImage(@RequestParam("imageFile") MultipartFile imageFile, @PathVariable Long housingId) {
        log.info("REST request to upload image to Housing#{}", housingId);
        return ResponseEntity.ok().body(imageService.addImageToHousing(imageFile, housingId));
    }

    @Operation(summary = "Get Image by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Image found", content = @Content(mediaType = "application/octet-stream")),
            @ApiResponse(responseCode = "404", description = "Image not found", content = @Content(mediaType = "*/*")),
    })
    @GetMapping("/{id}")
    public byte[] getImage(@PathVariable Long id) {
        log.info("REST request to get Image#{}", id);
        return imageService.getImage(id);
    }

}
