package com.reservation.backend.services;

import com.reservation.backend.entities.Housing;
import com.reservation.backend.entities.User;
import com.reservation.backend.exceptions.HousingAddException;
import com.reservation.backend.repositories.HousingRepository;
import com.reservation.backend.repositories.LocationRepository;
import com.reservation.backend.requests.HousingAddRequest;
import com.reservation.backend.security.JwtService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Time;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class HousingService {
    private final HousingRepository housingRepository;
    private final JwtService jwtService;
    private final LocationRepository locationRepository;

    public List<Housing> getAllHousings() {
        return housingRepository.findAll();
    }

    @Transactional
    public Optional<Housing> addHousing(HousingAddRequest housingAddRequest, String token) throws HousingAddException {
        Housing housing = new Housing();
        Optional<User> optionalUser = jwtService.getUserFromBearerToken(token);
        if (locationInDataBase(housingAddRequest.getLocation().getName())) {
            housing.setLocation(locationRepository.findByName(housingAddRequest.getLocation().getName()).orElseThrow());
        }
        if (housingAddRequest.getPricePerNight().compareTo(BigDecimal.ZERO) >= 0) {
            housing.setPricePerNight(housingAddRequest.getPricePerNight());
        }
        if (housingAddRequest.getPeople() >= 1) {
            housing.setPeople(housingAddRequest.getPeople());
        }
        if (checkInIsCorrect(housingAddRequest.getCheckIn())) {
            housing.setCheckInTime(housingAddRequest.getCheckIn());
        }
        if (housingAddRequest.getCheckOut().after(housingAddRequest.getCheckIn())) {
            housing.setCheckOutTime(housingAddRequest.getCheckOut());
        }
        if (housingAddRequest.getMinAgeToRent() > 0) {
            housing.setMinAgeToRent(housingAddRequest.getMinAgeToRent());
        }
        if (housingAddRequest.getRooms() > 0) {
            housing.setRooms(housingAddRequest.getRooms());
        }
        if (housingAddRequest.getM2() > 0) {
            housing.setM2(housingAddRequest.getM2());
        }
        if (housingAddRequest.getMinNights() >= 1) {
            housing.setMinNights(housingAddRequest.getMinNights());
        }
        Housing savedHousing = housingRepository.save(housing);
        return Optional.of(savedHousing);
    }

    private boolean locationInDataBase(String name) {
        return (locationRepository.findByName(name).isPresent());
    }

    private boolean checkInIsCorrect(Time time) {
        Date today = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(today);
        Calendar checkInCalendar = Calendar.getInstance();
        checkInCalendar.setTime(time);
        return checkInCalendar.before(calendar);
    }
}


