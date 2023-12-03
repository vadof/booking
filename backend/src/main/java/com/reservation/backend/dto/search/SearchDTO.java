package com.reservation.backend.dto.search;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

import static com.reservation.backend.config.SearchConstants.*;

@Getter
@Slf4j
public abstract class SearchDTO<T> {

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Integer size = DEFAULT_JPA_PAGE_SIZE;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Integer page = DEFAULT_JPA_PAGE_NUMBER;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String sortingFields = DEFAULT_SORTING_FIELDS;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Sort.Direction sortDirection = Sort.DEFAULT_DIRECTION;

    @JsonIgnore
    public Specification<T> getSpecification() {
        return (root, query, criteriaBuilder) -> {
            query.distinct(true);
            Predicate noFiltersApplied = criteriaBuilder.conjunction();
            List<Predicate> filters = new ArrayList<>();
            filters.add(noFiltersApplied);
            addFilters(root, query, criteriaBuilder,filters);
            return criteriaBuilder.and(filters.toArray(new Predicate[filters.size()]));
        };
    }

    @JsonIgnore
    public Pageable getPageable() {
        return PageRequest.of(page, size, getSortSpec());
    }

    @JsonIgnore
    public Sort getSortSpec() {
        String[] sortingFields = this.sortingFields.split(",");
        return (sortDirection == Sort.Direction.DESC) ?
                Sort.by(sortingFields).descending() : Sort.by(sortingFields).ascending();
    }

    protected void addFilters(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder, List<Predicate> filters) {
        log.debug("Override this method to provide additional filters in subclasses if needed");
    }

    public void setSize(Integer size) {
        this.size = size != null && size > 0 ? size : DEFAULT_JPA_PAGE_SIZE;
    }

    public void setPage(Integer page) {
        this.page = page != null && page >= 0 ? page : DEFAULT_JPA_PAGE_NUMBER;
    }

    public void setSortingFields(String sortingFields) {
        this.sortingFields = StringUtils.isNotBlank(sortingFields)
                ? sortingFields.replaceAll(" ", "") : DEFAULT_SORTING_FIELDS;
    }

    public void setSortDirection(Sort.Direction sortDirection) {
        this.sortDirection = sortDirection != null ? sortDirection : Sort.DEFAULT_DIRECTION;
    }
}
