package com.example.gotogether.repositories;

import com.example.gotogether.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    // Additional queries can be added if needed, such as filtering bookings by user or ride
}
