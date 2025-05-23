package com.example.gotogether.controllers;

import com.example.gotogether.dto.RideDTO;
import com.example.gotogether.services.RideService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/rides")
@RequiredArgsConstructor
public class RideController {

    private final RideService rideService;

    @GetMapping("/{id}")
    public ResponseEntity<RideDTO> getRide(@PathVariable Long id) {
        return ResponseEntity.ok(rideService.getRideById(id));
    }

    @PostMapping("/create")
    public ResponseEntity<RideDTO> createRide(@RequestBody RideDTO rideDTO)  {
        return ResponseEntity.ok(rideService.createRide(rideDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RideDTO> updateRide(@PathVariable Long id, @RequestBody RideDTO rideDTO) {
        return ResponseEntity.ok(rideService.updateRide(id, rideDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRide(@PathVariable Long id) {
        rideService.deleteRide(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("")
    public Page<RideDTO> getRides(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "price") String sortBy,
            @RequestParam(defaultValue = "desc") String direction
    ) {
        Sort sort = direction.equalsIgnoreCase("asc") ?
                Sort.by(sortBy).ascending() :
                Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(page, size, sort);
        return rideService.getAllRides(pageable);
    }

}
