package com.example.gotogether.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LocationDTO {
    private String name;
    private String arrivalTime;   // For waypoints and toLocation
    private String departureTime; // For fromLocation
}
