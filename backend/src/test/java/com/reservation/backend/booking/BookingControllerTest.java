package com.reservation.backend.booking;

import com.reservation.backend.GenericTest;
import com.reservation.backend.dto.BookingDTO;
import com.reservation.backend.dto.HousingDTO;
import com.reservation.backend.dto.ReviewDTO;
import com.reservation.backend.entities.Booking;
import com.reservation.backend.entities.Housing;
import com.reservation.backend.entities.Review;
import com.reservation.backend.entities.User;
import com.reservation.backend.mapper.HousingMapper;
import com.reservation.backend.mapper.ReviewMapper;
import com.reservation.backend.mapper.UserMapper;
import com.reservation.backend.repositories.BookingRepository;
import com.reservation.backend.repositories.HousingRepository;
import com.reservation.backend.repositories.ReviewRepository;
import com.reservation.backend.services.BookingService;
import com.reservation.backend.services.ReviewService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Optional;

import static net.bytebuddy.matcher.ElementMatchers.any;
import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
class BookingControllerTest extends GenericTest {
    @Autowired
    private MockMvc mockMvc;

    @Mock
    private BookingRepository bookingRepository;
    @Mock
    private HousingRepository housingRepository;

    @Spy
    private HousingMapper housingMapper = new HousingMapperImpl();

    @Spy
    private UserMapper userMapper;

    @InjectMocks
    private BookingService bookingService;

    @BeforeEach
    public void iniMapperDependencies() {
        UserMapper userMapper = new UserMapperImpl();
        ReflectionTestUtils.setField(housingMapper, "userMapper", userMapper);
    }

    @Test
    @Transactional
    void testBookHousingSuccessful() {
        BookingDTO bookingDTO = new BookingDTO();

        User tenant = User.builder().id(2L).email("email1").build();
        User owner = User.builder().id(1L).email("email2").build();

        Housing housing = Housing.builder().id(1L).build();
        housing.setOwner(owner);

        mockAuthenticatedUser(tenant);
        Mockito.when(housingRepository.findByIdAndPublishedTrue(housing.getId())).thenReturn(Optional.of(housing));
        Mockito.when(bookingRepository.save(ArgumentMatchers.any(Booking.class))).thenAnswer(invocation -> invocation.getArgument(0));

        BookingDTO bookedHousing = bookingService.bookHousing(housing.getId(), bookingDTO);
        Assertions.assertNotNull(bookedHousing);
    }
}