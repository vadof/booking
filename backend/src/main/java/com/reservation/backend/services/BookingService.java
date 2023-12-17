package com.reservation.backend.services;

import com.reservation.backend.dto.BookingDTO;
import com.reservation.backend.dto.PaginatedResponseDTO;
import com.reservation.backend.dto.search.BookingSearchDTO;
import com.reservation.backend.entities.Booking;
import com.reservation.backend.entities.Housing;
import com.reservation.backend.entities.User;
import com.reservation.backend.exceptions.AppException;
import com.reservation.backend.mapper.BookingMapper;
import com.reservation.backend.repositories.BookingRepository;
import com.reservation.backend.repositories.HousingRepository;
import com.reservation.backend.services.common.GenericService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingService extends GenericService {

    private final BookingRepository bookingRepository;
    private final BookingMapper bookingMapper;
    private final HousingRepository housingRepository;

    @Transactional
    public BookingDTO bookHousing(Long housingId, BookingDTO bookingDTO) {
        Housing housing = housingRepository.findById(housingId).orElseThrow(
                () -> new AppException("Housing#" + housingId + " not found", HttpStatus.NOT_FOUND));
        User tenant = getCurrentUserAsEntity();

        if (!dateOfHousingAvailableForBooking(housing, bookingDTO.getCheckInDate(), bookingDTO.getCheckOutDate())) {
            throw new AppException("Housing is not available for booking on the specified dates", HttpStatus.BAD_REQUEST);
        }

        if (bookingDTO.getCheckInDate().isBefore(LocalDate.now())
                || bookingDTO.getCheckInDate().isAfter(bookingDTO.getCheckOutDate())
                || bookingDTO.getCheckInDate().isEqual(bookingDTO.getCheckOutDate())) {
            throw new AppException("Invalid booking dates", HttpStatus.BAD_REQUEST);
        }

        Booking booking = bookingMapper.toEntity(bookingDTO);
        booking.setTenant(tenant);
        booking.setNights((int) ChronoUnit.DAYS.between(bookingDTO.getCheckInDate(), bookingDTO.getCheckOutDate()));
        booking.setTotalPrice(new BigDecimal(booking.getNights()).multiply(housing.getPricePerNight()));
        booking.setHousing(housing);

        bookingRepository.save(booking);

        return bookingMapper.toDto(booking);
    }

    // Only housing owner or tenant can get Booking
    public BookingDTO findBookingById(Long id) {
        Booking booking = getBookingById(id);
        User user = getCurrentUserAsEntity();

        if (booking.getTenant().equals(user) || booking.getHousing().getOwner().equals(user)) {
            return bookingMapper.toDto(booking);
        } else {
            throw new AppException("Access denied", HttpStatus.FORBIDDEN);
        }
    }

    public BookingDTO deleteBooking(Long id) {
        Booking booking = getBookingById(id);
        User user = getCurrentUserAsEntity();

        if (!user.equals(booking.getTenant())) {
            throw new AppException("Access denied", HttpStatus.FORBIDDEN);
        }

        LocalDate currentDate = LocalDate.now();
        if (currentDate.isAfter(booking.getCheckInDate()) || currentDate.isEqual(booking.getCheckInDate())) {
            throw new AppException("It is not possible to cancel a booking on the same day or after check-in", HttpStatus.FORBIDDEN);
        }

        bookingRepository.delete(booking);
        return bookingMapper.toDto(booking);
    }

    public PaginatedResponseDTO<BookingDTO> findAllUserBookings(BookingSearchDTO bookingSearchDTO) {
        User user = getCurrentUserAsEntity();
        bookingSearchDTO.setUserId(user.getId());
        Page<Booking> bookingPage = bookingRepository.findAll(bookingSearchDTO.getSpecification(), bookingSearchDTO.getPageable());
        List<BookingDTO> bookings = bookingMapper.toDtos(bookingPage.getContent());

        return PaginatedResponseDTO.<BookingDTO>builder()
                .page(bookingPage.getNumber())
                .totalPages(bookingPage.getTotalPages())
                .size(bookings.size())
                .sortingFields(bookingSearchDTO.getSortingFields())
                .sortDirection(bookingSearchDTO.getSortDirection().toString())
                .data(bookings)
                .build();
    }

    private boolean dateOfHousingAvailableForBooking(Housing housing, LocalDate checkIn, LocalDate checkOut) {
        return bookingRepository.findAllByDateRangeAndHousing(housing.getId(), checkIn, checkOut).size() == 0;
    }

    private Booking getBookingById(Long id) {
        return bookingRepository.findById(id).orElseThrow(
                () -> new AppException(String.format("Booking with id %s not found", id), HttpStatus.NOT_FOUND));
    }

}
