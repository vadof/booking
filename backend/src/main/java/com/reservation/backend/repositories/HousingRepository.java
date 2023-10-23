package com.reservation.backend.repositories;

import com.reservation.backend.entities.Housing;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.math.BigDecimal;
import java.util.List;

public interface HousingRepository extends JpaRepository<Housing, Long>, JpaSpecificationExecutor<Housing> {
    List<Housing> findAllByLocationNameContainingIgnoreCase(String locationName); // filter by name and price
    // AndPricePerNightGreaterThanEqual
    // query builder
    List<Housing> findAllByPublishedTrue();
}