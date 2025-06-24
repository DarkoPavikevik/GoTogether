package com.example.gotogether.repositories;

import com.example.gotogether.model.Booking;
import com.example.gotogether.model.Ride;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByRide(Ride ride);

    @Query("SELECT b.ride FROM Booking b WHERE b.user.id = :userId AND " +
            "(b.ride.date < :today OR (b.ride.date = :today AND b.ride.time < :nowTime))")
    List<Ride> findPastBookedRides(Long userId, LocalDate today, LocalTime nowTime);

    @Query("SELECT b.ride FROM Booking b WHERE b.user.id = :userId AND " +
            "(b.ride.date > :today OR (b.ride.date = :today AND b.ride.time >= :nowTime))")
    List<Ride> findFutureBookedRides(Long userId, LocalDate today, LocalTime nowTime);
}
