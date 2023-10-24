package com.reservation.backend.repositories;

import com.reservation.backend.entities.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    @Query("FROM Booking b WHERE b.housing.id = :housingId AND b.checkInDate < :checkOutDate AND b.checkOutDate > :checkInDate")
    List<Booking> findAllByDateRangeAndHousing(@Param("housingId") Long housingId,
                                               @Param("checkInDate") LocalDate checkInDate,
                                               @Param("checkOutDate") LocalDate checkOutDate);

}
