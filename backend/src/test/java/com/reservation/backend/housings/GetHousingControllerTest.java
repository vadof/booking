package com.reservation.backend.housings;

import com.reservation.backend.dto.HousingDTO;
import com.reservation.backend.entities.*;
import com.reservation.backend.mapper.HousingMapper;
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
    private HousingMapper housingMapper;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private HousingService housingService;
    private final Location tallinn = new Location(1L, "Tallinn", List.of(new Housing()));
    private final Location parnu = new Location(2L, "Parnu", List.of(new Housing()));
    private final Location paide = new Location(3L, "Paide", List.of(new Housing()));
    private final Location narva = new Location(1L, "Narva", List.of(new Housing()));
    private final HousingDTO housingDTOTallinn = new HousingDTO(5L, "Tallinn", null, tallinn, "55.34,31.321", new BigDecimal("30"), 5, new BigDecimal("5.3"), true);

    @Test
    public void testGetExample() throws Exception {
        List<HousingDTO> housingList = new ArrayList<>();
        housingList.add(housingDTOTallinn);
        when(housingService.getAllHousings(null, 0,0,0)).thenReturn(housingList);
        System.out.println(housingList);
        mockMvc.perform(get("/api/v1/housings").with(user("username")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Tallinn"))
                .andExpect(jsonPath("$[0].id").value("5")); // DTO format, list amount is same as amount od DTOs is returned

    }
}
