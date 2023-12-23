package com.reservation.backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserReviewDTO {
    private Long id;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private UserDTO user;
    private String email;
    private String review;
}
