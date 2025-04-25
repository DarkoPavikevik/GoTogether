package com.example.gotogether.services;

import com.example.gotogether.dto.RideDTO;
import com.example.gotogether.dto.UserInfoDTO;
import com.example.gotogether.dto.VehicleDTO;
import com.example.gotogether.exceptions.ResourceNotFoundException;
import com.example.gotogether.model.Ride;
import com.example.gotogether.model.User;
import com.example.gotogether.model.Vehicle;
import com.example.gotogether.repositories.RideRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RideService {

    @Autowired
    private final RideRepository rideRepository;

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
        dto.setDateTime(ride.getDateTime());
        dto.setPrice(ride.getPrice());
        dto.setSeatsAvailable(ride.getSeatsAvailable());
        dto.setStatus(ride.getStatus());
        dto.setBaggageInfo(ride.getBaggageInfo());
        dto.setWaypoints(ride.getWaypoints());
        dto.setNotes(ride.getNotes());

        if (ride.getDriver() != null) {
            UserInfoDTO userInfoDTO = new UserInfoDTO();
            userInfoDTO.setId(ride.getDriver().getId());
            userInfoDTO.setName(ride.getDriver().getName());
            userInfoDTO.setRating(ride.getDriver().getRating());
            dto.setDriver(userInfoDTO);
        }

        if (ride.getVehicle() != null) {
            VehicleDTO vehicleDTO = new VehicleDTO();
            vehicleDTO.setId(ride.getVehicle().getId());
            vehicleDTO.setBrand(ride.getVehicle().getBrand());
            vehicleDTO.setModel(ride.getVehicle().getModel());
            vehicleDTO.setPlateNumber(ride.getVehicle().getPlateNumber());
            vehicleDTO.setSeats(ride.getVehicle().getSeats());
            dto.setVehicle(vehicleDTO);
        }

        return dto;
    }

    public Ride mapToEntity(RideDTO dto) {
        Ride ride = new Ride();
        ride.setId(dto.getId());
        ride.setFromLocation(dto.getFromLocation());
        ride.setToLocation(dto.getToLocation());
        ride.setDateTime(dto.getDateTime());
        ride.setPrice(dto.getPrice());
        ride.setSeatsAvailable(dto.getSeatsAvailable());
        ride.setStatus(dto.getStatus());
        ride.setBaggageInfo(dto.getBaggageInfo());
        ride.setWaypoints(dto.getWaypoints());
        ride.setNotes(dto.getNotes());

        if (dto.getDriver() != null) {
            // Only setting ID – assuming JPA will manage the relationship
            var driver = new User();
            driver.setId(dto.getDriver().getId());
            ride.setDriver(driver);
        }

        if (dto.getVehicle() != null) {
            var vehicle = new Vehicle();
            vehicle.setId(dto.getVehicle().getId());
            ride.setVehicle(vehicle);
        }

        return ride;
    }
}
