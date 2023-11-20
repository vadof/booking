package com.reservation.backend.repositories;

import com.reservation.backend.dto.PriceDto;
import com.reservation.backend.entities.Housing;
import com.reservation.backend.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface HousingRepository extends JpaRepository<Housing, Long>, JpaSpecificationExecutor<Housing> {
    Optional<Housing> findByIdAndPublishedTrue(Long id);
    List<Housing> findByOwner(User owner);

    @Query("SELECT new com.reservation.backend.dto.PriceDto(MIN(h.pricePerNight), MAX(h.pricePerNight)) FROM Housing h")
    PriceDto getPrices();

}