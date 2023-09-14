package com.reservation.backend.repositories;

import com.reservation.backend.entities.Housing;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HousingRepository extends JpaRepository<Housing, Long> {
}
