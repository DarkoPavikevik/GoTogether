package com.example.gotogether.services;

import com.example.gotogether.dto.BookingDTO;
import com.example.gotogether.dto.BookingReuquestDTO;
import com.example.gotogether.dto.PassengerBookingDTO;
import com.example.gotogether.dto.UserInfoDTO;
import com.example.gotogether.enums.BookingStatus;
import com.example.gotogether.exceptions.ResourceNotFoundException;
import com.example.gotogether.model.Booking;
import com.example.gotogether.model.Ride;
import com.example.gotogether.model.User;
import com.example.gotogether.repositories.BookingRepository;
import com.example.gotogether.repositories.RideRepository;
import com.example.gotogether.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookingService {

    @Autowired
    private final BookingRepository bookingRepository;

    @Autowired
    private final EmailService emailService;

    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private final RideRepository rideRepository;

    public List<BookingDTO> getAllBookings() {
        return bookingRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public BookingDTO getBookingById(Long id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with id " + id));
        return mapToDTO(booking);
    }

    public List<PassengerBookingDTO> getConfirmedPassengersForRide(Long rideId) {
        Ride ride = rideRepository.findById(rideId)
                .orElseThrow(() -> new ResourceNotFoundException("Ride not found with id " + rideId));

        return bookingRepository.findByRide(ride).stream()
                .filter(booking -> booking.getStatus() == BookingStatus.CONFIRMED)
                .map(this::mapToPassengerDTO)
                .collect(Collectors.toList());
    }

    public BookingDTO createBooking(BookingDTO bookingDTO) {
        Booking booking = new Booking();
        booking.setUser(new User(bookingDTO.getUserId()));
        booking.setRide(new Ride(bookingDTO.getRideId()));
        booking.setNumberOfSeats(bookingDTO.getNumberOfSeats());
        booking.setStatus(BookingStatus.PENDING); // default status
        booking.setEmailSent(false); // email status initially false

        Booking saved = bookingRepository.save(booking);
        return mapToDTO(saved);
    }

    public BookingDTO updateBooking(Long id, BookingDTO bookingDTO) {
        Booking existingBooking = bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with id " + id));

        existingBooking.setStatus(bookingDTO.getStatus());
        existingBooking.setNumberOfSeats(bookingDTO.getNumberOfSeats());
        existingBooking.setEmailSent(bookingDTO.isEmailSent());

        Booking updated = bookingRepository.save(existingBooking);
        return mapToDTO(updated);
    }

    public void deleteBooking(Long id) {
        if (!bookingRepository.existsById(id)) {
            throw new ResourceNotFoundException("Booking not found with id " + id);
        }
        bookingRepository.deleteById(id);
    }

    public void requestToJoinRide(BookingReuquestDTO dto, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Ride ride = rideRepository.findById(dto.getRideId())
                .orElseThrow(() -> new RuntimeException("Ride not found"));

        Booking booking = Booking.builder()
                .ride(ride)
                .user(user)
                .numberOfSeats(dto.getNumberOfSeats())
                .status(BookingStatus.PENDING)
                .emailSent(true)
                .build();

        bookingRepository.save(booking);

        // Send email to ride owner
        emailService.sendBookingRequestEmail(ride.getDriver(), user, ride);
    }

    public BookingDTO mapToDTO(Booking booking) {
        BookingDTO dto = new BookingDTO();
        dto.setId(booking.getId());
        dto.setUserId(booking.getUser().getId()); // Getting the user ID
        dto.setRideId(booking.getRide().getId()); // Getting the ride ID
        dto.setNumberOfSeats(booking.getNumberOfSeats());
        dto.setStatus(booking.getStatus());
        dto.setEmailSent(booking.isEmailSent());
        return dto;
    }
    public PassengerBookingDTO mapToPassengerDTO(Booking booking) {
        return PassengerBookingDTO.builder()
                .user(UserInfoDTO.builder()
                        .id(booking.getUser().getId())
                        .name(booking.getUser().getUsername())
                        // Add any other fields if needed
                        .build())
                .numberOfSeats(booking.getNumberOfSeats())
                .status(booking.getStatus())
                .build();
    }
}
