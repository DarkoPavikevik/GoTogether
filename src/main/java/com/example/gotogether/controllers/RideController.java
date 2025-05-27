package com.example.gotogether.controllers;

import com.example.gotogether.dto.PopularRouteDTO;
import com.example.gotogether.dto.RideDTO;
import com.example.gotogether.services.RideService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

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

    @GetMapping("/search")
    public Page<RideDTO> searchRides(
            @RequestParam(required = false) String from,
            @RequestParam(required = false) String to,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "date") String sortBy,
            @RequestParam(defaultValue = "asc") String direction
    ) {
        Sort sort = direction.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);

        return rideService.searchRides(from, to, date, pageable);
    }


    @GetMapping("/popular")
    public ResponseEntity<List<PopularRouteDTO>> getPopularRoutes(
            @RequestParam(defaultValue = "6") int limit
    ) {
        return ResponseEntity.ok(rideService.getTopPopularRoutes(limit));
    }

}
