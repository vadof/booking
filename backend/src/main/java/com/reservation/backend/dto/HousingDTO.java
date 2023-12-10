package com.reservation.backend.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
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

    @NotBlank(message = "Name cannot be empty")
    private String name;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private ImageDTO previewImage;

    @NotNull(message = "Location cannot be empty")
    private LocationDTO location;

    @NotBlank(message = "Coordinates cannot be empty")
    private String coordinates;

    @NotNull(message = "PricePerNight cannot be null")
    @Positive(message = "Only positive number")
    private BigDecimal pricePerNight;

    @NotNull(message = "People cannot be null")
    @Positive(message = "Only positive number")
    private Integer people;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private BigDecimal rating;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Boolean published;

    @NotNull(message = "CheckIn cannot be empty")
    private Time checkIn;

    @NotNull(message = "CheckOut cannot be empty")
    private Time checkOut;

    @NotNull(message = "MinRentalAge cannot be null")
    @Positive(message = "Only positive number")
    private Integer minRentalAge;

    @NotEmpty(message = "Description cannot be empty")
    private String description;

    @NotNull(message = "Rooms cannot be null")
    @Positive(message = "Only positive number")
    private Integer rooms;

    @NotNull(message = "m2 cannot be null")
    @Positive(message = "Only positive number")
    private Integer m2;

    @NotNull(message = "MinNights cannot be null")
    @Positive(message = "Only positive number")
    private Integer minNights;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private UserDTO owner;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private List<ReviewDTO> reviews;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private List<ImageDTO> images;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private List<BookingDTO> bookings;
}
