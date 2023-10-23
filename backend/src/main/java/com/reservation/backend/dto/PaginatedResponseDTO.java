package com.reservation.backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaginatedResponseDTO<T> {
    private int page;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Integer totalPages;
    private int size;
    private String sortingFields;
    private String sortDirection;
    private List<T> data;
}
