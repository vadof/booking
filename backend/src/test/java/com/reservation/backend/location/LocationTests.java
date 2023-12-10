package com.reservation.backend.location;

import com.reservation.backend.dto.LocationDTO;
import com.reservation.backend.entities.Location;
import com.reservation.backend.mapper.LocationMapper;
import com.reservation.backend.repositories.LocationRepository;
import com.reservation.backend.services.LocationService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
class LocationTests {

    @Autowired
    private MockMvc mockMvc;


    @Mock
    private LocationRepository locationRepository;

    @Mock
    private LocationMapper locationMapper;

    @InjectMocks
    private LocationService locationService;

    @Test
    void testGetAllLocations() {
        Location location1 = new Location("Tallinn");
        Location location2 = new Location("Tartu");
        List<Location> locationList = Arrays.asList(location1, location2);

        when(locationRepository.findAll()).thenReturn(locationList);

        LocationDTO locationTallinn = new LocationDTO(1L, "Tallinn");
        LocationDTO locationTartu = new LocationDTO(2L, "Tartu");
        List<LocationDTO> locationDtoList = Arrays.asList(locationTallinn, locationTartu);

        when(locationMapper.toDtos(locationList)).thenReturn(locationDtoList);

        List<LocationDTO> result = locationService.getAllLocations();

        assertEquals(locationDtoList, result);

        verify(locationRepository, times(1)).findAll();

        verify(locationMapper, times(1)).toDtos(locationList);
    }

    @Test
    void getLocationControllerTest() throws Exception {
        List<LocationDTO> locationList = new ArrayList<>();
        LocationDTO locationTallinn = new LocationDTO(1L, "Tallinn");
        locationList.add(locationTallinn);
        Mockito.when(locationService.getAllLocations()).thenReturn(locationList);
        mockMvc.perform(get("/api/v1/locations").with(user("username")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Tallinn"))
                .andExpect(jsonPath("$[0].id").value("1"));


    }
}
