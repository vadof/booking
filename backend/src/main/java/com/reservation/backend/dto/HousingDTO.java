package com.reservation.backend.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.reservation.backend.entities.Booking;
import com.reservation.backend.entities.HousingDetails;
import com.reservation.backend.entities.Image;
import com.reservation.backend.entities.Location;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
@AllArgsConstructor
@Data // getter setter ++
public class HousingDTO {
    private Long id;
    private String name;
    private Location location;
    private BigDecimal rating;
}
