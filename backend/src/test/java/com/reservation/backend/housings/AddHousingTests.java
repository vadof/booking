package com.reservation.backend.housings;

import com.reservation.backend.controllers.HousingController;
import com.reservation.backend.entities.Housing;
import com.reservation.backend.repositories.HousingRepository;
import com.reservation.backend.services.HousingService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest

public class AddHousingTests {
    public HousingService housingService;
    public HousingRepository housingRepository;
    public HousingController housingController;


    @Test
    void getHousing() {
//        List<Housing> list1 = housingController.getAllHousings("Tallinn");
    }

}
