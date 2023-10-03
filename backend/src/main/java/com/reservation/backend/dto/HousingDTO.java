package com.reservation.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@AllArgsConstructor
@Data
public class HousingDTO {
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
