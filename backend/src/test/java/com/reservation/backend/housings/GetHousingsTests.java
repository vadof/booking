package com.reservation.backend.housings;

import com.reservation.backend.entities.*;
import com.reservation.backend.repositories.HousingRepository;
import com.reservation.backend.repositories.UserRepository;
import com.reservation.backend.requests.HousingAddRequest;
import com.reservation.backend.security.JwtService;
import com.reservation.backend.services.HousingService;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;


import java.math.BigDecimal;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
public class GetHousingsTests {
    @Autowired
    private MockMvc mockMvc;
    @InjectMocks
    public HousingService housingService;
    @Mock
    public HousingRepository housingRepository;
    private final Location tallinn = new Location(1L, "Tallinn", List.of(new Housing()));
    private final Location parnu = new Location(2L, "Parnu", List.of(new Housing()));
    private final Location paide = new Location(3L, "Paide", List.of(new Housing()));
    private final Location narva = new Location(1L, "Narva", List.of(new Housing()));
    private final Housing housingTallinn = new Housing(1L, new HousingDetails(), "Tallinn", new Image(), tallinn, "1.1, 7.8", new BigDecimal("10"), 4, new BigDecimal("8"), List.of(new Booking()));
    private final Housing housingParnu = new Housing(2L, new HousingDetails(), "House2", new Image(), parnu, "2.8, 5.9", new BigDecimal("20"), 3, new BigDecimal("8.8"), List.of(new Booking()));
    private final Housing housingPaide = new Housing(3L, new HousingDetails(), "House3", new Image(), paide, "4.6, 8.1", new BigDecimal("15"), 3, new BigDecimal("8.7"), List.of(new Booking()));
    private final Housing housingNarva = new Housing(4L, new HousingDetails(), "House4", new Image(), narva, "6.8, 5.8", new BigDecimal("25"), 6, new BigDecimal("8.9"), List.of(new Booking()));


    public void setup() {
        HousingAddRequest housingAddRequest = new HousingAddRequest();
        housingAddRequest.setName("Tallinn");
        housingAddRequest.setLocation(tallinn);
        housingAddRequest.setCoordinates("1.1, 7.8");
        housingAddRequest.setPricePerNight(new BigDecimal("10"));
        housingAddRequest.setPeople(4);
        housingAddRequest.setCheckIn(new Time(14L));
        housingAddRequest.setCheckOut(new Time(12L));
        housingAddRequest.setMinAgeToRent(18);
        housingAddRequest.setDescription("Description");
        housingAddRequest.setRooms(2);
        housingAddRequest.setM2(15);
        housingAddRequest.setMinNights(1);
        housingService.addHousing(housingAddRequest, "1");
    }


    @Test
    void getHousingCorrect() {
//        HousingAddRequest housingAddRequest = new HousingAddRequest("Tallinn", tallinn, "1.1, 7.8", new BigDecimal("10"), 4, new Time(14L), new Time(12L), 18, "Description", 2, 15, 1);
//        setup();
        List<Housing> housingList = new ArrayList<>();
        housingList.add(housingPaide);
        housingList.add(housingNarva);
        housingList.add(housingParnu);
        housingList.add(housingTallinn);
        when(housingRepository.findAll()).thenReturn(housingList); // get data from db with repository with mocked repository
        System.out.println(housingRepository.findAll());
        Assert.assertEquals(4, housingService.getAllHousings(null, 0, 0, 0).size());
        Assert.assertEquals(1, housingService.getAllHousings(tallinn.getName(), 0,0,0).size());
        Mockito.doReturn(List.of(housingTallinn));

    }
}
