package com.reservation.backend.services;

import com.reservation.backend.dto.HousingDTO;
import com.reservation.backend.dto.ImageDTO;
import com.reservation.backend.entities.Housing;
import com.reservation.backend.entities.Image;
import com.reservation.backend.entities.User;
import com.reservation.backend.exceptions.HousingAddException;
import com.reservation.backend.exceptions.UserNotFoundException;
import com.reservation.backend.mapper.HousingMapper;
import com.reservation.backend.mapper.ImageMapper;
import com.reservation.backend.repositories.HousingRepository;
import com.reservation.backend.repositories.ImageRepository;
import com.reservation.backend.repositories.LocationRepository;
import com.reservation.backend.requests.HousingAddRequest;
import com.reservation.backend.security.JwtService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

import java.util.List;

import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class HousingService {
    private final HousingMapper housingMapper;
    private final HousingRepository housingRepository;
    private final JwtService jwtService;
    private final LocationRepository locationRepository;
    private final ImageRepository imageRepository;
    private final ImageMapper imageMapper;

    public List<HousingDTO> getAllHousings(String locationName, int minPrice, int maxPrice, int amountPeople) {
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

        return housingMapper.toHousingDTOList(housingList);
    }

    @Transactional
    public Optional<HousingDTO> addHousing(HousingAddRequest housingAddRequest, String token) {
        try {
            Optional<User> userOptional = jwtService.getUserFromBearerToken(token);
            if (userOptional.isEmpty()) {
                throw new UserNotFoundException("User not found in the token");
            }
            User owner = userOptional.get();

            if (allHousingAddRequestFieldsAreCorrect(housingAddRequest)) {
                Housing housing = new Housing();
                this.saveHousing(housingAddRequest, housing, owner);

                log.info("Housing saved to database");
                return Optional.of(this.housingMapper.toHousingDTO(housing));
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

    @Transactional
    public Optional<HousingDTO> updateHousing(Long id, HousingAddRequest housingAddRequest, String token) {
        try {
            Housing housing = this.housingRepository.findById(id).orElseThrow();
            User owner = this.jwtService.getUserFromBearerToken(token).orElseThrow();

            if (housing.getOwner().equals(owner)
                    && allHousingAddRequestFieldsAreCorrect(housingAddRequest)) {
                this.saveHousing(housingAddRequest, housing, owner);
                log.info("Housing with id {} updated successfully", id);
                return Optional.of(this.housingMapper.toHousingDTO(housing));
            }
        } catch (Exception e) {
            log.error("Failed to update housing {}", e.getMessage());
        }
        return Optional.empty();
    }

    private boolean allHousingAddRequestFieldsAreCorrect(HousingAddRequest housingAddRequest) {
        return locationInDataBase(housingAddRequest.getLocation().getName())
                && (housingAddRequest.getPricePerNight().compareTo(BigDecimal.ZERO) >= 0)
                && (housingAddRequest.getPeople() >= 1)
                && (housingAddRequest.getMinAgeToRent() > 0)
                && (housingAddRequest.getRooms() > 0)
                && (housingAddRequest.getM2() > 0)
                && (housingAddRequest.getMinNights() >= 1);
    }

    private void saveHousing(HousingAddRequest housingAddRequest, Housing housing, User owner) {
        housing.setCheckIn(housingAddRequest.getCheckIn());
        housing.setCheckOut(housingAddRequest.getCheckOut());
        housing.setMinRentalAge(housingAddRequest.getMinAgeToRent());
        housing.setRooms(housingAddRequest.getRooms());
        housing.setM2(housingAddRequest.getM2());
        housing.setMinNights(housingAddRequest.getMinNights());
        housing.setDescription(housingAddRequest.getDescription());
        housing.setOwner(owner);
        housing.setLocation(locationRepository.findByName(housingAddRequest.getLocation().getName()).orElseThrow());
        housing.setPricePerNight(housingAddRequest.getPricePerNight());
        housing.setPeople(housingAddRequest.getPeople());
        housing.setCoordinates(housingAddRequest.getCoordinates().replaceAll(" ", ""));
        housing.setName(housingAddRequest.getName());
        housing.setPublished(false);

        this.housingRepository.save(housing);
    }

    public Optional<ImageDTO> changeImagePreview(Long housingId, Long imageId, String token) {
        try {
            Housing housing = this.housingRepository.findById(housingId).orElseThrow();
            User user = this.jwtService.getUserFromBearerToken(token).orElseThrow();
            Image image = this.imageRepository.findById(imageId).orElseThrow();

            if (image.getHousing().equals(housing) && housing.getOwner().equals(user)) {
                housing.setPreviewImage(image);
                this.housingRepository.save(housing);
                return Optional.of(this.imageMapper.toImageDTO(image));
            }
        } catch (Exception ignored) {}
        return Optional.empty();
    }

    public Optional<HousingDTO> publishHousing(Long housingId, String token, Boolean published) {
        Housing housing = this.housingRepository.findById(housingId).orElseThrow();
        User owner = this.jwtService.getUserFromBearerToken(token).orElseThrow();

        if (housing.getOwner().equals(owner)) {
            housing.setPublished(published);
            return Optional.of(this.housingMapper.toHousingDTO(housing));
        }
        return Optional.empty();
    }
}


