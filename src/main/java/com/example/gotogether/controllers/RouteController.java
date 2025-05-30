package com.example.gotogether.controllers;

import com.example.gotogether.dto.RideDTO;
import com.example.gotogether.dto.RideEstimateResponseDTO;
import com.example.gotogether.dto.RouteRequestDTO;
import com.example.gotogether.model.Ride;
import com.example.gotogether.repositories.RideRepository;
import com.example.gotogether.services.RideService;
import com.example.gotogether.services.RouteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/route")
public class RouteController {


    private final RideService rideService;
    private final RideRepository rideRepository;
    private final RouteService routeService;

    @GetMapping("/estimate/{rideId}")
    public ResponseEntity<?> getEstimatedTimeForRide(@PathVariable Long rideId) {
        try {
            Ride ride = rideRepository.findById(rideId)
                    .orElseThrow(() -> new RuntimeException("Ride not found with ID: " + rideId));

            List<String> cities = new ArrayList<>();
            cities.add(ride.getFromLocation());
            if (ride.getWaypoints() != null) {
                cities.addAll(ride.getWaypoints());
            }
            cities.add(ride.getToLocation());

            List<String> arrivalTimes = routeService.getEstimatedArrivalTimes(cities, ride.getTime());

            // ✅ Use your existing mapToDTO method from RideService
            RideDTO rideDTO = rideService.getRideById(rideId);

            RideEstimateResponseDTO responseDTO = RideEstimateResponseDTO.builder()
                    .estimatedArrivalTimes(arrivalTimes)
                    .build();

            return ResponseEntity.ok(responseDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error: " + e.getMessage());
        }
    }
}
