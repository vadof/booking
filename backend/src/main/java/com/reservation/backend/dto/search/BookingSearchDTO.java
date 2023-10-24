package com.reservation.backend.dto.search;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.reservation.backend.entities.Booking;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import java.util.List;

public class BookingSearchDTO extends SearchDTO<Booking> {

    @JsonIgnore
    private Long userId;

    @Override
    protected void addFilters(Root<Booking> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder, List<Predicate> filters) {
        filters.add(criteriaBuilder.equal(root.get("tenant").get("id"), this.userId));
    }

    public void setUserId(Long id) {
        this.userId = id;
    }
}
