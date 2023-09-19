package com.reservation.backend.repositories;

import com.reservation.backend.entities.Location;
import com.reservation.backend.services.HousingService;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LocationRepository extends JpaRepository<Location, Long> {
    // saab oma @Query lisada (sql)
    Optional<Location> findByName(String name);
}
