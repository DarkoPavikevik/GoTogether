package com.example.gotogether.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RideDTO {

    private Long id;
    private UserInfoDTO driver;
    private VehicleDTO vehicle;
    private String fromLocation;
    private String toLocation;
    private LocalDateTime dateTime;
    private double price;
    private int seatsAvailable;
    private String status;
    private String baggageInfo;
    private List<String> waypoints;
    private String notes;
}