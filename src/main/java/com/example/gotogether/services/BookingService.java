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
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
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




    private final String GEOCODE_URL = "https://api.openrouteservice.org/geocode/search";
    private final String API_KEY = "5b3ce3597851110001cf62488d8bf5fbf5134087a250246d98fe102c";
    private final RestTemplate restTemplate = new RestTemplate();
    private final Map<String, List<Double>> cachedCoordinates = new HashMap<>();

    private List<Double> getCoordinates(String location) {
        if (cachedCoordinates.containsKey(location)) {
            return cachedCoordinates.get(location);
        }

        String url = GEOCODE_URL + "?api_key=" + API_KEY + "&text=" + location + "&boundary.country=MK";

        var response = restTemplate.getForEntity(url, String.class);
        var json = new org.json.JSONObject(response.getBody());
        var features = json.getJSONArray("features");

        if (features.length() == 0) {
            throw new RuntimeException("No coordinates found for: " + location);
        }

        var coords = features.getJSONObject(0).getJSONObject("geometry").getJSONArray("coordinates");
        List<Double> coordList = List.of(coords.getDouble(0), coords.getDouble(1));
        cachedCoordinates.put(location, coordList);
        return coordList;
    }
    public List<BookingDTO> getAllBookings() {
        return bookingRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public List<BookingDTO> getBookingsForDriver(String username) {
        List<Booking> bookings = bookingRepository.findAllBookingsForDriver(username);
        return bookings.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public BookingDTO getBookingById(Long id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with id " + id));
        return mapToDTO(booking);
    }

    public List<BookingDTO> getBookingsByRideId(Long rideId) {
        List<Booking> bookings = bookingRepository.findByRideId(rideId);
        return bookings.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
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
        booking.setStatus(BookingStatus.PENDING); // default status
        booking.setEmailSent(false); // email status initially false
        booking.setDropoffLat(booking.getDropoffLat());
        booking.setDropoffLng(booking.getDropoffLng());
        booking.setDropoffLocation(booking.getDropoffLocation());
        booking.setPickupLat(booking.getPickupLat());
        booking.setPickupLng(booking.getPickupLng());
        booking.setPickupLocation(booking.getPickupLocation());
        booking.setNote(booking.getNote());

        Booking saved = bookingRepository.save(booking);
        return mapToDTO(saved);
    }

    public BookingDTO updateBooking(Long id, BookingDTO bookingDTO) {
        Booking existingBooking = bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with id " + id));

        if (bookingDTO.getStatus() == BookingStatus.CONFIRMED &&
                existingBooking.getStatus() != BookingStatus.CONFIRMED) {

            Ride ride = existingBooking.getRide();
            if (ride.getSeatsAvailable() > 0) {
                ride.setSeatsAvailable(ride.getSeatsAvailable() - 1);
                rideRepository.save(ride);
            } else {
                throw new IllegalStateException("No available seats for this ride");
            }
        }

        existingBooking.setStatus(bookingDTO.getStatus());
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

        List<Double> pickupCoords = getCoordinates(dto.getPickupLocation());
        List<Double> dropoffCoords = getCoordinates(dto.getDropoffLocation());

        Booking booking = Booking.builder()
                .ride(ride)
                .user(user)
                .pickupLocation(dto.getPickupLocation())
                .dropoffLocation(dto.getDropoffLocation())
                .pickupLat(pickupCoords.get(1))
                .pickupLng(pickupCoords.get(0))
                .dropoffLat(dropoffCoords.get(1))
                .dropoffLng(dropoffCoords.get(0))
                .status(BookingStatus.PENDING)
                .emailSent(true)
                .note(dto.getNote())
                .build();

        bookingRepository.save(booking);

        emailService.sendBookingRequestEmail(ride.getDriver(), user, ride);
    }

    public BookingDTO mapToDTO(Booking booking) {
        BookingDTO dto = new BookingDTO();
        dto.setId(booking.getId());
        dto.setUserId(booking.getUser().getId());
        dto.setPhoneNumber(booking.getUser().getPhoneNumber());
        dto.setUsername(booking.getUser().getUsername());// Getting the user ID
        dto.setRideId(booking.getRide().getId()); // Getting the ride ID
        dto.setStatus(booking.getStatus());
        dto.setEmailSent(booking.isEmailSent());
        dto.setPickupLocation(booking.getPickupLocation());
        dto.setDropoffLocation(booking.getDropoffLocation());
        dto.setNote(booking.getNote());
        return dto;
    }
    public PassengerBookingDTO mapToPassengerDTO(Booking booking) {
        return PassengerBookingDTO.builder()
                .user(UserInfoDTO.builder()
                        .id(booking.getUser().getId())
                        .name(booking.getUser().getUsername())
                        // Add any other fields if needed
                        .build())
                .status(booking.getStatus())
                .build();
    }

    public List<List<Double>> optimizeRideRoute(Long rideId) {
        Ride ride = rideRepository.findById(rideId)
                .orElseThrow(() -> new ResourceNotFoundException("Ride not found"));

        List<Booking> confirmedBookings = bookingRepository.findByRide(ride).stream()
                .filter(b -> b.getStatus() == BookingStatus.CONFIRMED)
                .collect(Collectors.toList());

        List<List<Double>> coordinates = confirmedBookings.stream()
                .flatMap(b -> List.of(
                        List.of(b.getPickupLng(), b.getPickupLat()),
                        List.of(b.getDropoffLng(), b.getDropoffLat())
                ).stream())
                .collect(Collectors.toList());

        return coordinates;
    }


}
