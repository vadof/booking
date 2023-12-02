package com.reservation.backend.services;

import com.reservation.backend.dto.*;
import com.reservation.backend.dto.search.HousingSearchDTO;
import com.reservation.backend.entities.Housing;
import com.reservation.backend.entities.Image;
import com.reservation.backend.entities.User;
import com.reservation.backend.exceptions.AppException;
import com.reservation.backend.mapper.HousingMapper;
import com.reservation.backend.mapper.HousingPreviewMapper;
import com.reservation.backend.mapper.ImageMapper;
import com.reservation.backend.repositories.HousingRepository;
import com.reservation.backend.repositories.ImageRepository;
import com.reservation.backend.repositories.LocationRepository;
import com.reservation.backend.repositories.UserRepository;
import com.reservation.backend.requests.HousingAddRequest;
import com.reservation.backend.services.common.GenericService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class HousingService extends GenericService {
    private final HousingPreviewMapper housingPreviewMapper;
    private final HousingMapper housingMapper;
    private final HousingRepository housingRepository;
    private final LocationRepository locationRepository;
    private final ImageRepository imageRepository;
    private final ImageMapper imageMapper;
    private final UserRepository userRepository;

    public PaginatedResponseDTO<HousingPreviewDTO> getAllHousings(HousingSearchDTO housingSearchDTO) {
        Page<Housing> housingPage = housingRepository.findAll(housingSearchDTO.getSpecification(), housingSearchDTO.getPageable());
        List<HousingPreviewDTO> housingPreviewDTOList = housingPreviewMapper.toDtos(housingPage.getContent());

        return PaginatedResponseDTO.<HousingPreviewDTO>builder()
                .page(housingPage.getNumber())
                .totalPages(housingPage.getTotalPages())
                .size(housingPreviewDTOList.size())
                .sortingFields(housingSearchDTO.getSortingFields())
                .sortDirection(housingSearchDTO.getSortDirection().toString())
                .data(housingPreviewDTOList)
                .build();
    }

    @Transactional
    public HousingDTO addHousing(HousingAddRequest housingAddRequest) {
        User owner = getCurrentUserAsEntity();

        if (allHousingAddRequestFieldsAreCorrect(housingAddRequest)) {
            Housing housing = new Housing();
            saveHousing(housingAddRequest, housing, owner);
            return housingMapper.toDto(housing);
        } else {
            throw new AppException("Invalid housing data", HttpStatus.BAD_REQUEST);
        }
    }

    private boolean locationInDataBase(String name) {
        return (locationRepository.findByName(name).isPresent());
    }

    @Transactional
    public HousingPreviewDTO updateHousing(Long id, HousingAddRequest housingAddRequest) {
        Housing housing = getHousing(id);
        User owner = getCurrentUserAsEntity();

        if (housing.getOwner().equals(owner)
                && allHousingAddRequestFieldsAreCorrect(housingAddRequest)) {
            saveHousing(housingAddRequest, housing, owner);
            log.info("Housing with id {} updated successfully", id);
            return housingPreviewMapper.toDto(housing);
        } else {
            throw new AppException("Forbidden", HttpStatus.FORBIDDEN);
        }
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

        housingRepository.save(housing);
    }

    public ImageDTO changeImagePreview(Long housingId, Long imageId) {
        Housing housing = getHousing(housingId);
        User user = getCurrentUserAsEntity();
        Image image = imageRepository.findById(imageId).orElseThrow(
                () -> new AppException("Image#" + imageId + " not found", HttpStatus.NOT_FOUND));

        if (image.getHousing().equals(housing) && housing.getOwner().equals(user)) {
            housing.setPreviewImage(image);
            housingRepository.save(housing);
            return imageMapper.toDto(image);
        } else {
            throw new AppException("Forbidden", HttpStatus.FORBIDDEN);
        }
    }

    public HousingPreviewDTO publishHousing(Long housingId, Boolean published) {
        Housing housing = getHousing(housingId);
        User owner = getCurrentUserAsEntity();

        if (housing.getOwner().equals(owner)) {
            housing.setPublished(published);
            housingRepository.save(housing);
            return housingPreviewMapper.toDto(housing);
        } else {
            throw new AppException("Forbidden", HttpStatus.FORBIDDEN);
        }
    }

    public HousingDTO getHousingById(Long id) {
        Housing housing = getHousing(id);
        if (!housing.isPublished()) {
            if (housing.getOwner().equals(getCurrentUserAsEntity())) {
                return housingMapper.toDto(housing);
            }
        } else {
            return housingMapper.toDto(housing);
        }

        throw new AppException("Forbidden", HttpStatus.FORBIDDEN);
    }

    public List<HousingPreviewDTO> getHousingsByOwner() {
        User owner = getCurrentUserAsEntity();
        List<Housing> housings = housingRepository.findByOwner(owner);
        return housingPreviewMapper.toDtos(housings);
    }

    @Transactional
    public HousingDTO deleteHousing(Long id) {
        Housing housing = getHousing(id);
        User user = getCurrentUserAsEntity();

        if (!housing.getOwner().equals(user)) {
            throw new AppException("Access denied", HttpStatus.FORBIDDEN);
        }

        HousingDTO dto = housingMapper.toDto(housing);
        housingRepository.delete(housing);
        return dto;
    }

    private Housing getHousing(Long id) {
        return housingRepository.findById(id).orElseThrow(() -> new AppException("Housing#" + id + " not found", HttpStatus.NOT_FOUND));
    }

    public PriceDto getHousingPrices() {
        return housingRepository.getPrices();
    }

    public HousingDTO addHousingToFavourites(Long housingId) {
        User user = getCurrentUserAsEntity();
        Housing housing = getHousing(housingId);
        user.getFavourites().add(housing);
        userRepository.save(user);
        return housingMapper.toDto(housing);
    }

    public List<HousingPreviewDTO> getAllFavourites() {
        return housingPreviewMapper.toDtos(getCurrentUserAsEntity().getFavourites());
    }

    public HousingDTO deleteFromFavourites(Long id) {
        User user = getCurrentUserAsEntity();
        Housing housing = getHousing(id);
        if (user.getFavourites().contains(housing)) {
            user.getFavourites().remove(housing);
            userRepository.save(user);
            return housingMapper.toDto(housing);
        } else {
            throw new AppException("Housing#" + id + " not in user's favourites", HttpStatus.NOT_FOUND);
        }

    }
}
