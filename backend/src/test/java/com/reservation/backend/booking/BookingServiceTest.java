package com.reservation.backend.booking;

import com.reservation.backend.GenericTest;
import com.reservation.backend.dto.BookingDTO;
import com.reservation.backend.dto.PaginatedResponseDTO;
import com.reservation.backend.dto.search.BookingSearchDTO;
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
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
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

        AppException ex = assertThrows(AppException.class,
                () -> bookingService.bookHousing(housing.getId(), bookingToSave));

        assertThat(ex.getHttpStatus()).isEqualTo(HttpStatus.BAD_REQUEST);

        verify(bookingRepository, times(0)).save(any());
        verify(bookingMapper, times(0)).toDto(any());
    }

    @Test
    @DisplayName("Book Housing Dates are not available Failure")
    void bookFailure2() {
        Housing housing = HousingMock.getHousingMock(1L);
        User tenant = UserMock.getUserMock(1L);

        when(housingRepository.findById(housing.getId())).thenReturn(Optional.of(housing));
        mockAuthenticatedUser(tenant);

        BookingDTO bookingToSave = BookingDtoMock.getBookingDtoMock(1L);

        when(bookingRepository.findAllByDateRangeAndHousing(housing.getId(), bookingToSave.getCheckInDate(),
                bookingToSave.getCheckOutDate())).thenReturn(Collections.singletonList(new Booking()));

        AppException ex = assertThrows(AppException.class,
                () -> bookingService.bookHousing(housing.getId(), bookingToSave));

        assertThat(ex.getHttpStatus()).isEqualTo(HttpStatus.BAD_REQUEST);

        verify(bookingRepository, times(0)).save(any());
        verify(bookingMapper, times(0)).toDto(any());
    }

    @Test
    @DisplayName("Book Housing CheckOut before CheckIn Failure")
    void bookFailure3() {
        Housing housing = HousingMock.getHousingMock(1L);
        User tenant = UserMock.getUserMock(1L);

        when(housingRepository.findById(housing.getId())).thenReturn(Optional.of(housing));
        mockAuthenticatedUser(tenant);

        BookingDTO bookingToSave = BookingDtoMock.getBookingDtoMock(1L);
        bookingToSave.setCheckInDate(LocalDate.now());
        bookingToSave.setCheckOutDate(bookingToSave.getCheckInDate().minusDays(1));

        AppException ex = assertThrows(AppException.class,
                () -> bookingService.bookHousing(housing.getId(), bookingToSave));

        assertThat(ex.getHttpStatus()).isEqualTo(HttpStatus.BAD_REQUEST);

        verify(bookingRepository, times(0)).save(any());
        verify(bookingMapper, times(0)).toDto(any());
    }

    @Test
    @DisplayName("Book Housing CheckIn before Current Date Failure")
    void bookFailure4() {
        Housing housing = HousingMock.getHousingMock(1L);
        User tenant = UserMock.getUserMock(1L);

        when(housingRepository.findById(housing.getId())).thenReturn(Optional.of(housing));
        mockAuthenticatedUser(tenant);

        BookingDTO bookingToSave = BookingDtoMock.getBookingDtoMock(1L);

        bookingToSave.setCheckInDate(LocalDate.now().minusDays(1));

        AppException ex = assertThrows(AppException.class,
                () -> bookingService.bookHousing(housing.getId(), bookingToSave));

        assertThat(ex.getHttpStatus()).isEqualTo(HttpStatus.BAD_REQUEST);

        verify(bookingRepository, times(0)).save(any());
        verify(bookingMapper, times(0)).toDto(any());
    }

    @Test
    @DisplayName("Find Booking By ID - Success for Tenant")
    void findBookingByIdSuccessForTenant() {
        Booking booking = new Booking();
        User tenant = UserMock.getUserMock(1L);
        User owner = UserMock.getUserMock(2L);
        mockAuthenticatedUser(tenant);

        when(bookingService.getCurrentUserAsEntity()).thenReturn(tenant);

        booking.setTenant(tenant);

        Housing housing = new Housing();
        housing.setOwner(owner);
        booking.setHousing(housing);

        when(bookingRepository.findById(anyLong())).thenReturn(Optional.of(booking));

        BookingDTO result = bookingService.findBookingById(1L);
        assertNotNull(result);
        verify(bookingMapper, times(1)).toDto(booking);
    }

    @Test
    @DisplayName("Find Booking By ID - Success for Owner")
    void findBookingByIdSuccessForOwner() {
        Booking booking = new Booking();
        User tenant = UserMock.getUserMock(1L);
        User owner = UserMock.getUserMock(2L);
        mockAuthenticatedUser(tenant);

        when(bookingService.getCurrentUserAsEntity()).thenReturn(tenant);

        booking.setTenant(tenant);

        Housing housing = new Housing();
        housing.setOwner(owner);
        booking.setHousing(housing);

        when(bookingRepository.findById(anyLong())).thenReturn(Optional.of(booking));
        BookingDTO result = bookingService.findBookingById(1L);

        assertNotNull(result);
        verify(bookingMapper, times(1)).toDto(any());
    }

    @Test
    @DisplayName("Find Booking By ID - Access Denied")
    void findBookingByIdAccessDenied() {
        Booking booking = new Booking();
        User unauthorizedUser = UserMock.getUserMock(3L);
        mockAuthenticatedUser(unauthorizedUser);

        when(bookingService.getCurrentUserAsEntity()).thenReturn(unauthorizedUser);

        User tenant = new User();
        User owner = new User();

        booking.setTenant(tenant);
        Housing housing = new Housing();
        housing.setOwner(owner);
        booking.setHousing(housing);

        when(bookingRepository.findById(anyLong())).thenReturn(Optional.of(booking));

        AppException ex = assertThrows(AppException.class,
                () -> bookingService.findBookingById(3L));

        assertEquals(HttpStatus.FORBIDDEN, ex.getHttpStatus());
        verify(bookingMapper, times(0)).toDto(any());
    }

    @Test
    @DisplayName("Delete Booking - Success")
    void deleteBookingSuccess() {
        Long bookingId = 2L;
        Booking booking = new Booking();
        booking.setId(bookingId);

        User user = new User();
        user.setId(1L);
        mockAuthenticatedUser(user);

        booking.setTenant(user);

        LocalDate currentDate = LocalDate.now();
        booking.setCheckInDate(currentDate.plusDays(1));

        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(booking));
        when(bookingMapper.toDto(booking)).thenReturn(new BookingDTO());

        when(bookingService.getCurrentUserAsEntity()).thenReturn(user);

        BookingDTO result = bookingService.deleteBooking(bookingId);

        verify(bookingRepository, times(1)).delete(booking);
        assertThat(result).isNotNull();
    }

    @Test
    @DisplayName("Delete Booking - Access Denied")
    void deleteBookingAccessDenied() {
        Long bookingId = 2L;
        Booking booking = new Booking();
        booking.setId(bookingId);

        User user = new User();
        user.setId(1L);
        mockAuthenticatedUser(user);

        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(booking));
        when(bookingService.getCurrentUserAsEntity()).thenReturn(user);

        Assertions.assertThrows(AppException.class, () -> bookingService.deleteBooking(bookingId), "Access denied");

        verify(bookingRepository, times(0)).delete(booking);
    }

    @Test
    @DisplayName("Find All User Bookings - Success")
    void findAllUserBookingsSuccess() {
        // TODO
        User user = UserMock.getUserMock(1L);
        mockAuthenticatedUser(user);
        BookingSearchDTO bookingSearchDTO = new BookingSearchDTO();

        Pageable pageable = PageRequest.of(0, 10);
        Page<Booking> bookingPage = new PageImpl<>(Collections.emptyList(), pageable, 0);
        when(bookingRepository.findAll(any(Specification.class), eq(pageable))).thenReturn(bookingPage);

        PaginatedResponseDTO<BookingDTO> result = bookingService.findAllUserBookings(bookingSearchDTO);

        verify(bookingRepository).findAll(any(Specification.class), eq(pageable));
        assertThat(result).isNotNull();
    }
}