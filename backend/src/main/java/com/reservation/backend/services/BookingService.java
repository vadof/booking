package com.reservation.backend.services;

import com.reservation.backend.dto.BookingDTO;
import com.reservation.backend.entities.Booking;
import com.reservation.backend.mapper.BookingMapper;
import com.reservation.backend.repositories.BookingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookingService {
    private final BookingRepository bookingRepository;
    private final BookingMapper bookingMapper;

    public List<BookingDTO> getAllBookings() {
        List<Booking> bookingList = bookingRepository.findAll();

        return bookingMapper.toBookingDTOList(bookingList);
    }
}
