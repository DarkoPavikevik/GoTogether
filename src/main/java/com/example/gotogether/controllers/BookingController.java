package com.example.gotogether.controllers;

import com.example.gotogether.dto.BookingDTO;
import com.example.gotogether.dto.BookingReuquestDTO;
import com.example.gotogether.model.Booking;
import com.example.gotogether.services.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
public class BookingController {


    private final BookingService bookingService;


    @GetMapping("")
    public ResponseEntity<List<BookingDTO>> getAllBookings() {
        return ResponseEntity.ok(bookingService.getAllBookings());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookingDTO> getBooking(@PathVariable Long id) {
        return ResponseEntity.ok(bookingService.getBookingById(id));
    }

    @PostMapping("/create")
    public ResponseEntity<BookingDTO> createBooking(@RequestBody BookingDTO booking) {
        return ResponseEntity.ok(bookingService.createBooking(booking));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookingDTO> updateBooking(@PathVariable Long id, @RequestBody BookingDTO booking) {
        return ResponseEntity.ok(bookingService.updateBooking(id, booking));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBooking(@PathVariable Long id) {
        bookingService.deleteBooking(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/request")
    public ResponseEntity<?> requestToJoinRide(@RequestBody BookingReuquestDTO bookingRequestDto, Principal principal) {
        bookingService.requestToJoinRide(bookingRequestDto, principal.getName());
        return ResponseEntity.ok("Booking request sent");
    }

    @GetMapping("/ride/{rideId}")
    public ResponseEntity<List<BookingDTO>> getBookingsByRideId(@PathVariable Long rideId) {
        List<BookingDTO> bookings = bookingService.getBookingsByRideId(rideId);
        return ResponseEntity.ok(bookings);
    }

}