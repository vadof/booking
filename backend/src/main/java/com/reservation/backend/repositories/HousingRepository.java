package com.reservation.backend.repositories;

import com.reservation.backend.entities.Housing;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.util.List;

public interface HousingRepository extends JpaRepository<Housing, Long> {
    List<Housing> findAllByLocationNameContainingIgnoreCase(String locationName); // filter by name and price
    // AndPricePerNightGreaterThanEqual
    // query builder
}