package com.reservation.backend.location;

import com.reservation.backend.dto.LocationDTO;
import com.reservation.backend.services.LocationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;


@SpringBootTest
@AutoConfigureMockMvc
public class LocationTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LocationService locationService;

    private final LocationDTO locationTallinn = new LocationDTO(1L, "Tallinn");

    @Test
    void getLocationControllerTest() throws Exception {
        List<LocationDTO> locationList = new ArrayList<>();
        locationList.add(locationTallinn);
        when(locationService.getAllLocations()).thenReturn(locationList);
        mockMvc.perform(get("/api/v1/locations").with(user("username")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Tallinn"))
                .andExpect(jsonPath("$[0].id").value("1"));


    }

}
