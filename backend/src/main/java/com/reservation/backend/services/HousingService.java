package com.reservation.backend.services;

import com.reservation.backend.entities.Housing;
import com.reservation.backend.entities.HousingDetails;
import com.reservation.backend.exceptions.HousingAddException;
import com.reservation.backend.repositories.HousingRepository;
import com.reservation.backend.repositories.LocationRepository;
import com.reservation.backend.requests.HousingAddRequest;
import com.reservation.backend.security.JwtService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Time;
import java.util.Calendar;
import java.util.List;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class HousingService {
    private final HousingRepository housingRepository;
    private final JwtService jwtService;
    private final LocationRepository locationRepository;

    public List<Housing> getAllHousings() {
        return housingRepository.findAll();
    }

    @Transactional
    public Optional<Housing> addHousing(HousingAddRequest housingAddRequest, String token) {
        try {
            if (locationInDataBase(housingAddRequest.getLocation().getName())
                    && (housingAddRequest.getPricePerNight().compareTo(BigDecimal.ZERO) >= 0)
                    && (housingAddRequest.getPeople() >= 1)
                    && (checkInIsCorrect(housingAddRequest.getCheckIn()))
                    && (checkOutIsLater(housingAddRequest.getCheckOut(), housingAddRequest.getCheckIn()))
                    && (housingAddRequest.getMinAgeToRent() > 0)
                    && (housingAddRequest.getRooms() > 0)
                    && (housingAddRequest.getM2() > 0)
                    && (housingAddRequest.getMinNights() >= 1)) {

                Housing housing = new Housing();

                HousingDetails housingDetails = new HousingDetails();
                housingDetails.setCheckIn(housingAddRequest.getCheckIn());
                housingDetails.setCheckOut(housingAddRequest.getCheckOut());
                housingDetails.setMinAgeToRent(housingAddRequest.getMinAgeToRent());
                housingDetails.setRooms(housingAddRequest.getRooms());
                housingDetails.setM2(housingAddRequest.getM2());
                housingDetails.setMinNights(housingAddRequest.getMinNights());

                housing.setLocation(locationRepository.findByName(housingAddRequest.getLocation().getName()).orElseThrow());
                housing.setPricePerNight(housingAddRequest.getPricePerNight());
                housing.setPeople(housingAddRequest.getPeople());
                housing.setHousingDetails(housingDetails);

                Housing savedHousing = housingRepository.save(housing);
                log.info("Housing saved to database");
                return Optional.of(savedHousing);
            } else {
                throw new HousingAddException("Invalid housing data");
            }
        } catch (HousingAddException e) {
            log.error("Error adding housing to database: " + e.getMessage());
            return Optional.empty();
        }
    }

    private boolean locationInDataBase(String name) {
        return (locationRepository.findByName(name).isPresent());
    }

    private boolean checkInIsCorrect(Time checkInTime) {
        Calendar now = Calendar.getInstance();
        Calendar checkInCalendar = Calendar.getInstance();
        checkInCalendar.setTime(checkInTime);

        int nowHour = now.get(Calendar.HOUR_OF_DAY);
        int nowMinute = now.get(Calendar.MINUTE);
        int checkInHour = checkInCalendar.get(Calendar.HOUR_OF_DAY);
        int checkInMinute = checkInCalendar.get(Calendar.MINUTE);

        return checkInHour > nowHour || (checkInHour == nowHour && checkInMinute >= nowMinute);
    }

    private boolean checkOutIsLater(Time checkOutTime, Time checkInTime) {
        Calendar checkOutCalendar = Calendar.getInstance();
        checkOutCalendar.setTime(checkOutTime);
        Calendar checkInCalendar = Calendar.getInstance();
        checkInCalendar.setTime(checkInTime);

        int checkOutHour = checkOutCalendar.get(Calendar.HOUR_OF_DAY);
        int checkOutMinute = checkOutCalendar.get(Calendar.MINUTE);
        int checkInHour = checkInCalendar.get(Calendar.HOUR_OF_DAY);
        int checkInMinute = checkInCalendar.get(Calendar.MINUTE);

        return checkOutHour > checkInHour || (checkOutHour == checkInHour && checkOutMinute > checkInMinute);
    }
}


