package com.reservation.backend.booking;

import com.reservation.backend.GenericTest;
import com.reservation.backend.dto.BookingDTO;
import com.reservation.backend.entities.Booking;
import com.reservation.backend.entities.Housing;
import com.reservation.backend.entities.User;
import com.reservation.backend.exceptions.AppException;
import com.reservation.backend.mapper.*;
import com.reservation.backend.mocks.dto.BookingDtoMock;
import com.reservation.backend.mocks.entity.HousingMock;
import com.reservation.backend.mocks.entity.UserMock;
import com.reservation.backend.repositories.BookingRepository;
import com.reservation.backend.repositories.HousingRepository;
import com.reservation.backend.services.BookingService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookingServiceTest extends GenericTest {

    @Mock
    BookingRepository bookingRepository;

    @Mock
    HousingRepository housingRepository;

    @Spy
    private BookingMapper bookingMapper = new BookingMapperImpl();

    @Captor
    private ArgumentCaptor<Booking> bookingArgumentCaptor;

    @InjectMocks
    BookingService bookingService;

    @BeforeEach
    public void iniMapperDependencies() {
        UserMapper userMapper = new UserMapperImpl();
        HousingPreviewMapper housingPreviewMapper = new HousingPreviewMapperImpl();
        ImageMapper imageMapper = new ImageMapperImpl();
        LocationMapper locationMapper = new LocationMapperImpl();

        ReflectionTestUtils.setField(bookingMapper, "userMapper", userMapper);
        ReflectionTestUtils.setField(bookingMapper, "housingPreviewMapper", housingPreviewMapper);

        ReflectionTestUtils.setField(housingPreviewMapper, "imageMapper", imageMapper);
        ReflectionTestUtils.setField(housingPreviewMapper, "locationMapper", locationMapper);
    }

    @Test
    @DisplayName("Book Housing Success")
    void bookSuccess1() {
        Housing housing = HousingMock.getHousingMock(1L);
        User tenant = UserMock.getUserMock(1L);

        when(housingRepository.findById(housing.getId())).thenReturn(Optional.of(housing));
        mockAuthenticatedUser(tenant);

        BookingDTO bookingToSave = BookingDtoMock.getBookingDtoMock(1L);

        when(bookingRepository.findAllByDateRangeAndHousing(housing.getId(), bookingToSave.getCheckInDate(),
                bookingToSave.getCheckOutDate())).thenReturn(new ArrayList<>());

        bookingService.bookHousing(housing.getId(), bookingToSave);

        verify(bookingRepository).save(bookingArgumentCaptor.capture());
        Booking savedEntity = bookingArgumentCaptor.getValue();

        verify(bookingMapper, times(1)).toEntity(bookingToSave);
        verify(bookingMapper, times(1)).toDto(savedEntity);

        assertThat(savedEntity.getNights()).isEqualTo(1);
        assertThat(savedEntity.getTotalPrice()).isEqualTo(housing.getPricePerNight());
        assertThat(savedEntity.getTenant()).isEqualTo(tenant);
        assertThat(savedEntity.getAdditionalInfo()).isEqualTo(bookingToSave.getAdditionalInfo());
        assertThat(savedEntity.getCheckInDate()).isEqualTo(bookingToSave.getCheckInDate());
        assertThat(savedEntity.getCheckOutDate()).isEqualTo(bookingToSave.getCheckOutDate());
    }

    @Test
    @DisplayName("Book Housing CheckIn equals CheckOut Failure")
    void bookFailure1() {
        Housing housing = HousingMock.getHousingMock(1L);
        User tenant = UserMock.getUserMock(1L);

        when(housingRepository.findById(housing.getId())).thenReturn(Optional.of(housing));
        mockAuthenticatedUser(tenant);

        BookingDTO bookingToSave = BookingDtoMock.getBookingDtoMock(1L);
        bookingToSave.setCheckInDate(LocalDate.of(LocalDate.now().getYear(), 12, 1));
        bookingToSave.setCheckOutDate(LocalDate.of(LocalDate.now().getYear(), 12, 1));

        when(bookingRepository.findAllByDateRangeAndHousing(housing.getId(), bookingToSave.getCheckInDate(),
                bookingToSave.getCheckOutDate())).thenReturn(new ArrayList<>());

        AppException ex = Assertions.assertThrows(AppException.class,
                () -> bookingService.bookHousing(housing.getId(), bookingToSave));

        assertThat(ex.getHttpStatus()).isEqualTo(HttpStatus.BAD_REQUEST);

        verify(bookingRepository, times(0)).save(any());
        verify(bookingMapper, times(0)).toDto(any());
    }

    @Test
    @DisplayName("Book Housing Dates are not available Failure")
    void bookFailure2() {

    }

    @Test
    @DisplayName("Book Housing CheckOut before CheckIn Failure")
    void bookFailure3() {

    }

    @Test
    @DisplayName("Book Housing CheckIn after Current Date Failure")
    void bookFailure4() {

    }

}