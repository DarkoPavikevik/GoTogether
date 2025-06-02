package com.example.gotogether.services;

import com.example.gotogether.dto.*;
import com.example.gotogether.enums.Currency;
import com.example.gotogether.enums.LuggageSize;
import com.example.gotogether.exceptions.ResourceNotFoundException;
import com.example.gotogether.model.Review;
import com.example.gotogether.model.Ride;
import com.example.gotogether.model.User;
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
    private final RouteService routeService; // Inject routeService here

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

    private RideDTO mapToDTO(Ride ride) {
        UserInfoDTO userInfoDTO = null;

        if (ride.getDriver() != null) {
            User driver = ride.getDriver();

            // Convert driver's reviews to DTOs
            List<ReviewDTO> reviewDTOs = driver.getReviews().stream()
                    .map(review -> ReviewDTO.builder()
                            .id(review.getId())
                            .reviewerId(review.getId()) // assuming this field exists
                            .commentDate(review.getCommentDate())
                            .comment(review.getComment())
                            .rating(review.getRating())
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
