package com.reservation.backend.services;

import com.reservation.backend.mapper.BookingMapper;
import com.reservation.backend.repositories.BookingRepository;
import com.reservation.backend.repositories.HousingRepository;
import com.reservation.backend.security.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class FavoriteService {

    private final BookingRepository bookingRepository;
    private final BookingMapper bookingMapper;
    private final HousingRepository housingRepository;
    private final JwtService jwtService;

}
