package com.reservation.backend.housings;

import com.reservation.backend.dto.HousingPreviewDTO;
import com.reservation.backend.dto.LocationDTO;
import com.reservation.backend.mapper.HousingPreviewMapper;
import com.reservation.backend.services.HousingService;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
@AutoConfigureMockMvc
@SpringBootTest
public class GetHousingControllerTest {
    @Mock
    private HousingPreviewMapper housingMapper;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private HousingService housingService;
    private final LocationDTO tallinn = new LocationDTO(1L, "Tallinn");
    private final LocationDTO parnu = new LocationDTO(2L, "Parnu");
    private final LocationDTO paide = new LocationDTO(3L, "Paide");
    private final LocationDTO narva = new LocationDTO(1L, "Narva");
    private final HousingPreviewDTO housingDTOTallinn = new HousingPreviewDTO(5L, "Tallinn", null, tallinn, "55.34,31.321", new BigDecimal("30"), 5, new BigDecimal("5.3"), true);

    @Test
    public void testGetExample() throws Exception {
        List<HousingPreviewDTO> housingList = new ArrayList<>();
        housingList.add(housingDTOTallinn);
        when(housingService.getAllHousings(null, 0,0,0)).thenReturn(housingList);
        System.out.println(housingList);
        mockMvc.perform(get("/api/v1/housings").with(user("username")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Tallinn"))
                .andExpect(jsonPath("$[0].id").value("5")); // DTO format, list amount is same as amount od DTOs is returned

    }
}
