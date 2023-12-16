package com.reservation.backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class ReviewDTO {
    private Long id;

    @NotEmpty(message = "Field cannot be empty")
    private String text;

    @NotNull(message = "Field cannot be empty")
    @Min(value = 1, message = "Min rating is 1")
    @Max(value = 10, message = "Max rating is 10")
    private Integer rating;

    private LocalDate date;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private UserDTO reviewer;
}
