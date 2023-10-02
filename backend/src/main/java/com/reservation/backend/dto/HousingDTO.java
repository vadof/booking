package com.reservation.backend.dto;

import com.reservation.backend.entities.Image;
import com.reservation.backend.entities.Location;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@AllArgsConstructor
@Data
public class HousingDTO {
    private Long id;
    private String name;
    private Image previewImage;
    private Location location;
    private String coordinates;
    private BigDecimal pricePerNight;
    private Integer people;
    private BigDecimal rating;
    private Boolean published;
}
