package com.reservation.backend.services;

import com.reservation.backend.dto.BookingDTO;
import com.reservation.backend.entities.Booking;
import com.reservation.backend.entities.Housing;
import com.reservation.backend.entities.User;
import com.reservation.backend.exceptions.AppException;
import com.reservation.backend.mapper.BookingMapper;
import com.reservation.backend.repositories.BookingRepository;
import com.reservation.backend.repositories.HousingRepository;
import com.reservation.backend.security.JwtService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final BookingRepository bookingRepository;
    private final BookingMapper bookingMapper;
    private final HousingRepository housingRepository;
    private final JwtService jwtService;

    @Transactional
    public BookingDTO bookHousing(Long housingId, BookingDTO bookingDTO, String token) {
        Housing housing = this.housingRepository.findById(housingId).orElseThrow();
        User tenant = this.jwtService.getUserFromBearerToken(token).orElseThrow();

        if (!dateOfHousingAvailableForBooking(housing, bookingDTO.getCheckInDate(), bookingDTO.getCheckOutDate())) {
            throw new AppException("Housing is not available for booking on the specified dates", HttpStatus.BAD_REQUEST);
        }

        if (bookingDTO.getCheckInDate().isBefore(LocalDate.now())
                || bookingDTO.getCheckInDate().isAfter(bookingDTO.getCheckOutDate())
                || bookingDTO.getCheckInDate().isEqual(bookingDTO.getCheckOutDate())) {
            throw new AppException("Invalid booking dates", HttpStatus.BAD_REQUEST);
        }

        Booking booking = this.bookingMapper.toEntity(bookingDTO);
        booking.setTenant(tenant);
        booking.setNights((int) ChronoUnit.DAYS.between(bookingDTO.getCheckInDate(), bookingDTO.getCheckOutDate()));
        booking.setTotalPrice(new BigDecimal(booking.getNights()).multiply(housing.getPricePerNight()));
        booking.setHousing(housing);

        this.bookingRepository.save(booking);

        return this.bookingMapper.toDto(booking);
    }

    // Only housing owner or tenant can get Booking
    public BookingDTO findBookingById(Long id, String token) {
        Booking booking = this.getBookingById(id);
        User user = this.jwtService.getUserFromBearerToken(token).orElseThrow();

        if (!booking.getTenant().equals(user) || !booking.getHousing().getOwner().equals(user)) {
            throw new AppException("Access denied", HttpStatus.FORBIDDEN);
        }

        return this.bookingMapper.toDto(booking);
    }

    private boolean dateOfHousingAvailableForBooking(Housing housing, LocalDate checkIn, LocalDate checkOut) {
        return this.bookingRepository.findAllByDateRangeAndHousing(housing.getId(), checkIn, checkOut).size() == 0;
    }

    private Booking getBookingById(Long id) {
        return this.bookingRepository.findById(id).orElseThrow(
                () -> new AppException(String.format("Booking with id %s not found", id), HttpStatus.NOT_FOUND));
    }

}
