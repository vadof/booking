package com.reservation.backend.services;

import com.reservation.backend.dto.BookingDTO;
import com.reservation.backend.entities.Booking;
import com.reservation.backend.entities.User;
import com.reservation.backend.mapper.BookingMapper;
import com.reservation.backend.repositories.BookingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookingService {
    private final BookingRepository bookingRepository;
    private final BookingMapper bookingMapper;

    public Optional<BookingDTO> getAllBookings(User user) {
        List<Booking> bookingList = bookingRepository.findAllByUser(user);
        if (bookingList.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of((BookingDTO) bookingMapper.toDtos(bookingList));
    }
}
