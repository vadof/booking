package com.reservation.backend.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingDTO {
    private Long id;
    private UserDTO tenant;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private Integer nights;
    private String additionalInfo;
}
