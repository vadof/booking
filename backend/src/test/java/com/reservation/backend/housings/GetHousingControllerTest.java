package com.reservation.backend.housings;

import com.reservation.backend.dto.HousingDTO;
import com.reservation.backend.entities.*;
import com.reservation.backend.mapper.HousingMapper;
import com.reservation.backend.services.HousingService;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Spy;
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
    private final Housing housingTallinn = new Housing(1L, new HousingDetails(), "Tallinn", new Image(), tallinn, "1.1, 7.8", new BigDecimal("10"), 4, new BigDecimal("8"), List.of(new Booking()));
    private final Housing housingParnu = new Housing(2L, new HousingDetails(), "Parnu", new Image(), parnu, "2.8, 5.9", new BigDecimal("20"), 3, new BigDecimal("8.8"), List.of(new Booking()));
    private final Housing housingPaide = new Housing(3L, new HousingDetails(), "Paide", new Image(), paide, "4.6, 8.1", new BigDecimal("15"), 3, new BigDecimal("8.7"), List.of(new Booking()));
    private final Housing housingNarva = new Housing(4L, new HousingDetails(), "Narva", new Image(), narva, "6.8, 5.8", new BigDecimal("25"), 6, new BigDecimal("8.9"), List.of(new Booking()));
    private final HousingDTO housingDTOTallinn = new HousingDTO(5L, "Tallinn", tallinn, new BigDecimal("8"));

    @Test
    public void testGetExample() throws Exception {
        List<HousingDTO> housingList = new ArrayList<>();
//        housingList.add(housingMapper.toHousingDTO(housingPaide));
//        housingList.add(housingMapper.toHousingDTO(housingNarva));
//        housingList.add(housingMapper.toHousingDTO(housingParnu));
//        housingList.add(housingMapper.toHousingDTO(housingTallinn));
        housingList.add(housingDTOTallinn);
        when(housingService.getAllHousings(null, 0,0,0)).thenReturn(housingList);
        System.out.println(housingList);
        mockMvc.perform(get("/api/v1/housings").with(user("username")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Tallinn"))
                .andExpect(jsonPath("$[0].id").value("5")); // DTO format, list amount is same as amount od DTOs is returned

    }
}
