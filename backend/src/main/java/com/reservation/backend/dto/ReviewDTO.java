package com.reservation.backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewDTO {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @NotEmpty(message = "Field cannot be empty")
    private String text;

    @NotNull(message = "Field cannot be empty")
    @Min(value = 1, message = "Min rating is 1")
    @Max(value = 10, message = "Max rating is 10")
    private Integer rating;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private UserDTO reviewer;
}
