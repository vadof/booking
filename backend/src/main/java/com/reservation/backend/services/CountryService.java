package com.reservation.backend.services;

import com.reservation.backend.dto.CountryDTO;
import com.reservation.backend.dto.HousingPreviewDTO;
import com.reservation.backend.dto.PaginatedResponseDTO;
import com.reservation.backend.dto.search.CountrySearchDTO;
import com.reservation.backend.dto.search.HousingSearchDTO;
import com.reservation.backend.entities.Country;
import com.reservation.backend.entities.Housing;
import com.reservation.backend.mapper.CountryMapper;
import com.reservation.backend.repositories.CountryRepository;
import com.reservation.backend.services.common.GenericService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CountryService extends GenericService {

    private final CountryRepository countryRepository;
    private final CountryMapper countryMapper;

//    public List<CountryDTO> getAllCountry() {
//        List<Country> countryList = countryRepository.findAll();
//        return countryMapper.toDtos(countryList);
//    }

    public PaginatedResponseDTO<CountryDTO> getAllCountries(CountrySearchDTO housingSearchDTO) {
        Page<Country> housingPage = countryRepository.findAll(housingSearchDTO.getSpecification(), housingSearchDTO.getPageable());
        List<CountryDTO> countriesDTOList = countryMapper.toDtos(housingPage.getContent());
        System.out.println(housingSearchDTO.getKood() + housingSearchDTO.getNimi());
        countriesDTOList = countriesDTOList.stream()
                .filter(country -> (country.getKood().equals(housingSearchDTO.getKood()))
                        && (country.getNimi().equals(housingSearchDTO.getNimi())))
                .collect(Collectors.toList());

        return PaginatedResponseDTO.<CountryDTO>builder()
                .page(housingPage.getNumber())
                .totalPages(housingPage.getTotalPages())
                .size(countriesDTOList.size())
                .sortingFields(housingSearchDTO.getSortingFields())
                .sortDirection(housingSearchDTO.getSortDirection().toString())
                .data(countriesDTOList)
                .build();
    }
}
