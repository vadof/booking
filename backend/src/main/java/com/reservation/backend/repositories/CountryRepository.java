package com.reservation.backend.repositories;

import com.reservation.backend.entities.Country;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CountryRepository extends JpaRepository<Country, Long> {
    Page<Country> findAll(Specification<Country> specification, Pageable pageable);
    Page<Country> findCountriesByKood(String kood, Pageable pageable);
}
