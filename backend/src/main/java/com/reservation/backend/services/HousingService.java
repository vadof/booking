package com.reservation.backend.services;

import com.reservation.backend.entities.Housing;
import com.reservation.backend.entities.HousingDetails;
import com.reservation.backend.entities.User;
import com.reservation.backend.exceptions.HousingAddException;
import com.reservation.backend.exceptions.UserNotFoundException;
import com.reservation.backend.repositories.HousingDetailsRepository;
import com.reservation.backend.repositories.HousingRepository;
import com.reservation.backend.repositories.LocationRepository;
import com.reservation.backend.requests.HousingAddRequest;
import com.reservation.backend.security.JwtService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.sql.Time;
import java.util.Calendar;
import java.util.List;

import java.util.Optional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class HousingService {
    private final HousingRepository housingRepository;
    private final JwtService jwtService;
    private final LocationRepository locationRepository;

    private final HousingDetailsRepository housingDetailsRepository;
    private final Logger logger = LoggerFactory.getLogger(HousingService.class);

    public List<Housing> getAllHousings(String locationName, int minPrice, int maxPrice, int amountPeople) {
        List<Housing> housingList = housingRepository.findAll();
        if (locationName != null) {
            housingList = housingList.stream().filter(h -> h.getName() != null &&  h.getName().equals(locationName)).collect(Collectors.toList());
        }
        if (minPrice != 0) {
            housingList = housingList.stream().filter(h -> h.getPricePerNight().compareTo(new BigDecimal(minPrice)) >= 0).collect(Collectors.toList());
        }
        if (maxPrice != 0) {
            housingList = housingList.stream().filter(h -> h.getPricePerNight().compareTo(new BigDecimal(maxPrice)) < 0).collect(Collectors.toList());
        }
        if (amountPeople != 0) {
            housingList = housingList.stream().filter(h -> h.getPeople() <= amountPeople).collect(Collectors.toList());
        }
        return housingList;
    }

    @Transactional
    public Optional<Housing> addHousing(HousingAddRequest housingAddRequest, String token) {
        try {
            Optional<User> userOptional = jwtService.getUserFromBearerToken(token);
            if (userOptional.isEmpty()) {
                throw new UserNotFoundException("User not found in the token");
            }
            User owner = userOptional.get();

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
                housingDetails.setMinRentalAge(housingAddRequest.getMinAgeToRent());
                housingDetails.setRooms(housingAddRequest.getRooms());
                housingDetails.setM2(housingAddRequest.getM2());
                housingDetails.setMinNights(housingAddRequest.getMinNights());
                housingDetails.setDescription(housingAddRequest.getDescription());
                housingDetails.setOwner(owner);

                housingDetails = housingDetailsRepository.save(housingDetails);

                housing.setHousingDetails(housingDetails);
                housing.setLocation(locationRepository.findByName(housingAddRequest.getLocation().getName()).orElseThrow());
                housing.setPricePerNight(housingAddRequest.getPricePerNight());
                housing.setPeople(housingAddRequest.getPeople());
                housing.setCoordinates(housingAddRequest.getCoordinates());
                housing.setName(housingAddRequest.getName());
                housing.setRating(BigDecimal.valueOf(10));

                Housing savedHousing = housingRepository.save(housing);
                log.info("Housing saved to database");
                return Optional.of(savedHousing);
            } else {
                throw new HousingAddException("Invalid housing data");
            }
        } catch (HousingAddException e) {
            log.error("Error adding housing to database: " + e.getMessage());
            return Optional.empty();
        } catch (UserNotFoundException e) {
            log.error("User not found in the token error: " + e.getMessage());
            throw new RuntimeException(e);
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


