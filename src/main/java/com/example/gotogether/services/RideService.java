package com.example.gotogether.services;

import com.example.gotogether.dto.RideDTO;
import com.example.gotogether.dto.UserInfoDTO;
import com.example.gotogether.dto.VehicleDTO;
import com.example.gotogether.exceptions.ResourceNotFoundException;
import com.example.gotogether.model.Ride;
import com.example.gotogether.model.User;
import com.example.gotogether.model.Vehicle;
import com.example.gotogether.repositories.RideRepository;
import com.example.gotogether.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RideService {

    @Autowired
    private final RideRepository rideRepository;

    @Autowired
    private final UserRepository userRepository;

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
        Ride ride = mapToEntity(rideDTO);
        Ride saved = rideRepository.save(ride);
        return mapToDTO(saved);
    }

    public RideDTO updateRide(Long id, RideDTO rideDTO) {
        Ride existing = rideRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ride not found with id " + id));

        Ride rideToUpdate = mapToEntity(rideDTO);
        rideToUpdate.setId(id); // Ensure the ID is preserved
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
        RideDTO dto = new RideDTO();
        dto.setId(ride.getId());
        dto.setFromLocation(ride.getFromLocation());
        dto.setToLocation(ride.getToLocation());
        dto.setDate(ride.getDate());
        dto.setTime(ride.getTime());
        dto.setPrice(ride.getPrice());
        dto.setCurrency(ride.getCurrency());
        dto.setLuggageSize(ride.getLuggageSize());
        dto.setSeatsAvailable(ride.getSeatsAvailable());
        dto.setStatus(ride.getStatus());
        dto.setWaypoints(ride.getWaypoints());
        dto.setNotes(ride.getNotes());
        if (ride.getDriver() != null) {
            dto.setUserId(ride.getDriver().getId());
        }


        return dto;
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

        if (dto.getUserId() != null) {
            User user = userRepository.findById(dto.getUserId())
                    .orElseThrow(() -> new RuntimeException("User not found"));
            ride.setUser(user);
        }

        return ride;
    }
}
