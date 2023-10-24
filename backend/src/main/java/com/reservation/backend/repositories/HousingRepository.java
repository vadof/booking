package com.reservation.backend.repositories;

import com.reservation.backend.entities.Housing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface HousingRepository extends JpaRepository<Housing, Long>, JpaSpecificationExecutor<Housing> {
    Optional<Housing> findByIdAndPublishedTrue(Long id);
}