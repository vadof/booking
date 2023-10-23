package com.reservation.backend.dto.search;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.reservation.backend.config.Constants;
import com.reservation.backend.entities.Booking;
import com.reservation.backend.entities.Housing;
import jakarta.persistence.criteria.*;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
public class HousingSearchDTO extends SearchDTO<Housing> {

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String location;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Integer people;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Integer rooms;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private BigDecimal minPrice;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private BigDecimal maxPrice;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @DateTimeFormat(pattern = Constants.DATE_FORMAT_DD_MM_YYYY)
    private LocalDate checkInDate;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @DateTimeFormat(pattern = Constants.DATE_FORMAT_DD_MM_YYYY)
    private LocalDate checkOutDate;

    @Override
    protected void addFilters(Root<Housing> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder, List<Predicate> filters) {
        filters.add(criteriaBuilder.isTrue(root.get("published")));

        if (StringUtils.isNotEmpty(location)) {
            filters.add(criteriaBuilder.equal(root.get("location").get("name"), location));
        }

        if (people != null) {
            filters.add(criteriaBuilder.greaterThanOrEqualTo(root.get("people"), people));
        }

        if (rooms != null) {
            filters.add(criteriaBuilder.greaterThanOrEqualTo(root.get("rooms"), rooms));
        }

        if (minPrice != null) {
            filters.add(criteriaBuilder.greaterThanOrEqualTo(root.get("pricePerNight"), minPrice));
        }

        if (maxPrice != null) {
            filters.add(criteriaBuilder.lessThanOrEqualTo(root.get("pricePerNight"), maxPrice));
        }

        if (checkInDate != null && checkOutDate != null) {
            Subquery<Long> subquery = query.subquery(Long.class);
            Root<Booking> bookingRoot = subquery.from(Booking.class);
            subquery.select(bookingRoot.get("housing").get("id"));

            Predicate checkInPredicate = criteriaBuilder.between(bookingRoot.get("checkInDate"),
                    criteriaBuilder.literal(checkInDate), criteriaBuilder.literal(checkOutDate));
            Predicate checkOutPredicate = criteriaBuilder.between(bookingRoot.get("checkOutDate"),
                    criteriaBuilder.literal(checkInDate), criteriaBuilder.literal(checkOutDate));
            subquery.where(checkInPredicate, checkOutPredicate);

            filters.add(criteriaBuilder.not(root.get("id").in(subquery)));
        }
    }
}
