package com.reservation.backend.dto;


import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.sql.Time;
import java.util.List;

@Data
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
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
    private Time checkIn;
    private Time checkOut;
    private Integer minRentalAge;
    private String description;
    private Integer rooms;
    private Integer m2;
    private Integer minNights;
    private UserDTO owner;
    private List<ReviewDTO> reviews;
    private List<ImageDTO> images;
    private List<BookingDTO> bookings;
}
