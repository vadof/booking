package com.reservation.backend.requests;

import com.reservation.backend.entities.Location;
import lombok.Data;

import java.math.BigDecimal;
import java.sql.Time;

@Data
public class HousingAddRequest {
    String name;
    Location location;
    String coordinates;
    BigDecimal pricePerNight;
    Integer people;
    Time checkIn;
    Time checkOut;
    Integer minAgeToRent;
    String description;
    Integer rooms;
    Integer m2;
    Integer minNights;
}
