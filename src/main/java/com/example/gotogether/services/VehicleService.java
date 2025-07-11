package com.example.gotogether.services;

import com.example.gotogether.exceptions.ResourceNotFoundException;
import com.example.gotogether.model.User;
import com.example.gotogether.model.Vehicle;
import com.example.gotogether.repositories.UserRepository;
import com.example.gotogether.repositories.VehicleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


import com.example.gotogether.dto.VehicleDTO;
import com.example.gotogether.exceptions.ResourceNotFoundException;
import com.example.gotogether.model.User;
import com.example.gotogether.model.Vehicle;
import com.example.gotogether.repositories.UserRepository;
import com.example.gotogether.repositories.VehicleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VehicleService {

    @Autowired
    private final VehicleRepository vehicleRepository;

    @Autowired
    private final UserRepository userRepository;

    public VehicleDTO createVehicle(VehicleDTO dto) {
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Vehicle vehicle = new Vehicle();
        vehicle.setBrand(dto.getBrand());
        vehicle.setPlateNumber(dto.getPlateNumber());
        vehicle.setModel(dto.getModel());
        vehicle.setPicture(dto.getPicture());
        vehicle.setColor(dto.getColor());
        vehicle.setSeats(dto.getSeats());
        vehicle.setAirCondition(dto.getAirCondition());
        vehicle.setUsbCharging(dto.getUsbCharging());
        vehicle.setMusic(dto.getMusic());
        vehicle.setComfortableSeats(dto.getComfortableSeats());
        vehicle.setUser(user);

        Vehicle saved = vehicleRepository.save(vehicle);
        return mapToDTO(saved);
    }

    public List<VehicleDTO> getVehiclesByUser(Long userId) {
        return vehicleRepository.findByUserId(userId).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public VehicleDTO getVehicleById(Long id) {
        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found"));
        return mapToDTO(vehicle);
    }

    public void deleteVehicle(Long id) {
        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found"));
        vehicleRepository.delete(vehicle);
    }

    public VehicleDTO editVehicle(Long id, VehicleDTO dto) {

        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found"));


        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        vehicle.setBrand(dto.getBrand());
        vehicle.setPlateNumber(dto.getPlateNumber());
        vehicle.setModel(dto.getModel());
        vehicle.setColor(dto.getColor());
        vehicle.setPicture(dto.getPicture());
        vehicle.setYear(dto.getYear());
        vehicle.setSeats(dto.getSeats());
        vehicle.setAirCondition(dto.getAirCondition());
        vehicle.setUsbCharging(dto.getUsbCharging());
        vehicle.setMusic(dto.getMusic());
        vehicle.setComfortableSeats(dto.getComfortableSeats());
        vehicle.setUser(user);
        Vehicle updatedVehicle = vehicleRepository.save(vehicle);

        // Return the updated vehicle as a DTO
        return mapToDTO(updatedVehicle);
    }

    private VehicleDTO mapToDTO(Vehicle vehicle) {
        VehicleDTO dto = new VehicleDTO();
        dto.setBrand(vehicle.getBrand());
        dto.setModel(vehicle.getModel());
        dto.setPlateNumber(vehicle.getPlateNumber());
        dto.setColor(vehicle.getColor());
        dto.setPicture(vehicle.getPicture());
        dto.setSeats(vehicle.getSeats());
        dto.setAirCondition(vehicle.getAirCondition());
        dto.setUsbCharging(vehicle.getUsbCharging());
        dto.setMusic(vehicle.getMusic());
        dto.setComfortableSeats(vehicle.getComfortableSeats());
        dto.setUserId(vehicle.getUser().getId());
        return dto;
    }
}

