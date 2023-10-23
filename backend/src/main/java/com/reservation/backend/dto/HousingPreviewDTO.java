package com.reservation.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

@AllArgsConstructor
@Data
@SuperBuilder(toBuilder=true)
@NoArgsConstructor
public class HousingPreviewDTO {
    private Long id;
    private String name;
    private ImageDTO previewImage;
    private LocationDTO location;
    private String coordinates;
    private BigDecimal pricePerNight;
    private Integer people;
    private BigDecimal rating;
    private Boolean published;
}
