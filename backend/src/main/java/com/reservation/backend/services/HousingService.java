package com.reservation.backend.services;

import com.reservation.backend.entities.Housing;
import com.reservation.backend.repositories.HousingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class HousingService {
    private final HousingRepository housingRepository;

    @Autowired
    public HousingService(HousingRepository housingRepository) {
        this.housingRepository = housingRepository;
    }

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
}


