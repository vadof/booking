package com.reservation.backend.repositories;

import com.reservation.backend.entities.Booking;
import com.reservation.backend.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findAllByTenant(User user);

}
