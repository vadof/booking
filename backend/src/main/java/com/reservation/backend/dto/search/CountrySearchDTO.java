package com.reservation.backend.dto.search;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.reservation.backend.entities.Country;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
@Data

public class CountrySearchDTO extends SearchDTO<Country> {

//    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String kood;
//    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String nimi;

    @Override
    protected void addFilters(Root<Country> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder, List<Predicate> filters) {
        if (kood != null) {
            filters.add(criteriaBuilder.greaterThanOrEqualTo(root.get("kood"), kood));
        }

        if (nimi != null) {
            filters.add(criteriaBuilder.greaterThanOrEqualTo(root.get("nimi"), nimi));
        }
    }
}
