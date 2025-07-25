package com.example.gotogether.services;

import com.example.gotogether.dto.*;
import com.example.gotogether.enums.Currency;
import com.example.gotogether.enums.LuggageSize;
import com.example.gotogether.exceptions.ResourceNotFoundException;
import com.example.gotogether.model.Review;
import com.example.gotogether.model.Ride;
import com.example.gotogether.model.User;
import com.example.gotogether.model.Vehicle;
import com.example.gotogether.repositories.BookingRepository;
import com.example.gotogether.repositories.RideRepository;
import com.example.gotogether.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.gotogether.model.RideSpecification.*;

@Service
@RequiredArgsConstructor
public class RideService {

    @Autowired
    private final RideRepository rideRepository;

    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private final RouteService routeService;

    @Autowired
    private final BookingService bookingService;

    @Autowired
    private final BookingRepository bookingRepository;

    public List<RideDTO> getAllRides() {
        return rideRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public RideDTO getRideById(Long id) {
        Ride ride = rideRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ride not found with id " + id));
        return mapToDTO(ride);
    }

    public Page<RideDTO> getAllRides(Pageable pageable) {
        return rideRepository.findAll(pageable)
                .map(this::mapToDTO);
    }

    public RideDTO createRide(RideDTO rideDTO) {
        User driver = userRepository.findById(rideDTO.getUserInfo().getId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id " + rideDTO.getUserInfo().getId()));

        driver.setNumberOfRides(driver.getNumberOfRides() + 1);
        userRepository.save(driver);

        Ride ride = mapToEntity(rideDTO);
        ride.setDriver(driver);

        Ride saved = rideRepository.save(ride);
        return mapToDTO(saved);
    }

    public RideDTO updateRide(Long id, RideDTO rideDTO) {
        Ride existing = rideRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ride not found with id " + id));

        Ride rideToUpdate = mapToEntity(rideDTO);
        rideToUpdate.setId(id);
        Ride updated = rideRepository.save(rideToUpdate);
        return mapToDTO(updated);
    }

    public void deleteRide(Long id) {
        if (!rideRepository.existsById(id)) {
            throw new ResourceNotFoundException("Ride not found with id " + id);
        }
        rideRepository.deleteById(id);
    }

    public List<Ride> getRidesByDriver(Long driverId)
    {
        return rideRepository.findByDriverId(driverId);
    }

    public List<UserRideDTO> getPastRidesForUser(Long userId) {
        LocalDate today = LocalDate.now();
        LocalTime nowTime = LocalTime.now();

        List<Ride> driverRides = rideRepository.findPastRidesByUser(userId, today, nowTime);
        List<Ride> bookedRides = bookingRepository.findPastBookedRides(userId, today, nowTime);

        List<UserRideDTO> combined = new ArrayList<>();
        driverRides.forEach(ride -> combined.add(new UserRideDTO(mapToDTO(ride), true)));
        bookedRides.forEach(ride -> combined.add(new UserRideDTO(mapToDTO(ride), false)));

        return combined;
    }

    public List<UserRideDTO> getFutureRidesForUser(Long userId) {
        LocalDate today = LocalDate.now();
        LocalTime nowTime = LocalTime.now();

        List<Ride> driverRides = rideRepository.findFutureRidesByUser(userId, today, nowTime);
        List<Ride> bookedRides = bookingRepository.findFutureBookedRides(userId, today, nowTime);

        List<UserRideDTO> combined = new ArrayList<>();
        driverRides.forEach(ride -> combined.add(new UserRideDTO(mapToDTO(ride), true)));
        bookedRides.forEach(ride -> combined.add(new UserRideDTO(mapToDTO(ride), false)));

        return combined;
    }


    RideDTO mapToDTO(Ride ride) {
        UserInfoDTO userInfoDTO = null;
        VehicleDTO vehicleDTO = null;
        List<PassengerBookingDTO> confirmedPassengers = bookingService.getConfirmedPassengersForRide(ride.getId());


        if (ride.getDriver() != null) {
            User driver = ride.getDriver();

            // Convert driver's reviews to DTOs
            List<ReviewDTO> reviewDTOs = driver.getReviews().stream()
                    .map(review -> ReviewDTO.builder()
                            .id(review.getId())
                            .reviewerId(review.getId()) // assuming this field exists
                            .reviewedUserId(review.getReviewedUser().getId())
                            .commentDate(review.getCommentDate())
                            .comment(review.getComment())
                            .rating(review.getRating())
                            .reviewerName(review.getReviewer().getUsername())
                            .reviewerPicture(review.getReviewer().getProfilePicture())
                            .build())
                    .collect(Collectors.toList());

            userInfoDTO = UserInfoDTO.builder()
                    .id(driver.getId())
                    .name(driver.getUsername())
                    .avatar(driver.getProfilePicture())
                    .vehicle(driver.getVehicle().getBrand())
                    .rating(String.valueOf(driver.getRating()))
                    .numberOfRides(driver.getNumberOfRides())
                    .reviews(reviewDTOs) // include the driver's reviews
                    .build();
        }

        VehicleDTO vechicleDTO = null;
        if (ride.getDriver() != null && ride.getDriver().getVehicle() != null) {
            Vehicle vehicle = ride.getDriver().getVehicle();
            vehicleDTO = VehicleDTO.builder()
                    .id(vehicle.getId())
                    .userId(vehicle.getUser().getId())
                    .brand(vehicle.getBrand())
                    .picture(vehicle.getPicture())
                    .model(vehicle.getModel())
                    .year(vehicle.getYear())
                    .color(vehicle.getColor())
                    .plateNumber(vehicle.getPlateNumber())
                    .seats(vehicle.getSeats())
                    .airCondition(vehicle.getAirCondition())
                    .usbCharging(vehicle.getUsbCharging())
                    .music(vehicle.getMusic())
                    .comfortableSeats(vehicle.getComfortableSeats())
                    .build();
        }



        // Prepare cities list
        List<String> cities = new ArrayList<>();
        cities.add(ride.getFromLocation());
        if (ride.getWaypoints() != null && !ride.getWaypoints().isEmpty()) {
            cities.addAll(ride.getWaypoints());
        }
        cities.add(ride.getToLocation());

        // Estimate
        List<String> arrivalTimes = routeService.getEstimatedArrivalTimes(cities, ride.getTime());
        RideEstimateResponseDTO estimate = RideEstimateResponseDTO.builder()
                .estimatedArrivalTimes(arrivalTimes)
                .build();

        return RideDTO.builder()
                .id(ride.getId())
                .userInfo(userInfoDTO)
                .vehicle(vehicleDTO)
                .estimate(estimate)
                .fromLocation(ride.getFromLocation())
                .toLocation(ride.getToLocation())
                .date(ride.getDate())
                .time(ride.getTime())
                .price(ride.getPrice())
                .seatsAvailable(ride.getSeatsAvailable())
                .status(ride.getStatus())
                .luggageSize(ride.getLuggageSize())
                .currency(ride.getCurrency())
                .waypoints(ride.getWaypoints())
                .notes(ride.getNotes())
                .passengerBookings(confirmedPassengers)
                .build();
    }


    public Ride mapToEntity(RideDTO dto) {
        Ride ride = new Ride();
        ride.setId(dto.getId());
        ride.setFromLocation(dto.getFromLocation());
        ride.setToLocation(dto.getToLocation());
        ride.setDate(dto.getDate());
        ride.setTime(dto.getTime());
        ride.setPrice(dto.getPrice());
        ride.setSeatsAvailable(dto.getSeatsAvailable());
        ride.setStatus(dto.getStatus());
        ride.setLuggageSize(dto.getLuggageSize());
        ride.setCurrency(dto.getCurrency());
        ride.setWaypoints(dto.getWaypoints());
        ride.setNotes(dto.getNotes());
        return ride;
    }

    public Page<RideDTO> searchRides(String from, String to, LocalDate date, Pageable pageable) {
        Specification<Ride> spec = Specification.where(hasFromLocation(from))
                .and(hasToLocation(to))
                .and(hasDate(date));

        return rideRepository.findAll(spec, pageable)
                .map(this::mapToDTO);
    }

    public List<PopularRouteDTO> getTopPopularRoutes(int limit) {
        Pageable pageable = PageRequest.of(0, limit);
        return rideRepository.findTopPopularRoutes(pageable);
    }


}
