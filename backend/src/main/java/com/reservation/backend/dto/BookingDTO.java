package com.reservation.backend.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.reservation.backend.config.Constants;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookingDTO {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @NotNull(message = "Field cannot be empty")
    @JsonFormat(pattern = Constants.DATE_FORMAT_DD_MM_YYYY)
    private LocalDate checkInDate;
    @NotNull(message = "Field cannot be empty")
    @JsonFormat(pattern = Constants.DATE_FORMAT_DD_MM_YYYY)
    private LocalDate checkOutDate;

    private String additionalInfo;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Integer nights;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private BigDecimal totalPrice;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private UserDTO tenant;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private HousingPreviewDTO housing;
}
