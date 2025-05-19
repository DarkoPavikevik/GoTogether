package com.example.gotogether.controllers;

import com.example.gotogether.services.CityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/cities")
@RequiredArgsConstructor
public class CityController {

    private final CityService cityService;

    @GetMapping("/{country}")
    public ResponseEntity<List<String>> getCities(@PathVariable String country) {
        List<String> cities = cityService.getCitiesForCountry(country);
        return ResponseEntity.ok(cities);
    }
}