package com.reservation.backend.services;

import com.reservation.backend.entities.Housing;
import com.reservation.backend.repositories.HousingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HousingService {
    private final HousingRepository housingRepository;

    @Autowired
    public HousingService(HousingRepository housingRepository) {
        this.housingRepository = housingRepository;
    }

    public List<Housing> getAllHousings() {
        return housingRepository.findAll();
    }
}


