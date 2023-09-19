package com.reservation.backend.requests;

import com.reservation.backend.entities.Housing;
import com.reservation.backend.entities.Location;

import java.math.BigDecimal;
import java.sql.Time;

public class HousingAddRequest {
    String name;
    Location location; // is in database
    String coordinates;
    BigDecimal pricePerNight; // not neg
    Integer people; // >= 1
    Time checkIn; // not today or earlier
    Time checkOut; // must be later than check in
    Integer minAgeToRent; // not neg
    String description;
    Integer rooms; // not neg
    Integer m2; // not neg
    Integer minNights; // min 1

    public Location getLocation() {
        return location;
    }

    public BigDecimal getPricePerNight() {
        return pricePerNight;
    }

    public Time getCheckIn() {
        return checkIn;
    }

    public Time getCheckOut() {
        return checkOut;
    }

    public Integer getPeople() {
        return people;
    }

    public Integer getMinAgeToRent() {
        return minAgeToRent;
    }

    public Integer getRooms() {
        return rooms;
    }

    public Integer getM2() {
        return m2;
    }

    public Integer getMinNights() {
        return minNights;
    }
}
