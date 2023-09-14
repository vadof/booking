package com.reservation.backend.repositories;

import com.reservation.backend.entities.HousingDetails;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HousingDetailsRepository extends JpaRepository<HousingDetails, Long> {
}
