package com.example.gotogether.controllers;

import com.example.gotogether.dto.VehicleDTO;
import com.example.gotogether.services.VehicleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vehicles")
@RequiredArgsConstructor
public class VehicleController {

    private final VehicleService vehicleService;


    @PostMapping("/user/{userId}")
    public ResponseEntity<VehicleDTO> createVehicle( @RequestBody VehicleDTO vehicle) {
        return ResponseEntity.ok(vehicleService.createVehicle( vehicle));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<VehicleDTO>> getUserVehicles(@PathVariable Long userId) {
        return ResponseEntity.ok(vehicleService.getVehiclesByUser(userId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<VehicleDTO> getVehicle(@PathVariable Long id) {
        return ResponseEntity.ok(vehicleService.getVehicleById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVehicle(@PathVariable Long id) {
        vehicleService.deleteVehicle(id);
        return ResponseEntity.noContent().build();
    }

}
