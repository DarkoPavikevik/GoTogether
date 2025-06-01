package com.example.gotogether.dto;

import com.example.gotogether.enums.Currency;
import com.example.gotogether.enums.LuggageSize;
import com.example.gotogether.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RideDTO {

    private Long id;
    private UserInfoDTO userInfo;
    private VehicleDTO vehicleDTO;
    private RideEstimateResponseDTO estimate;
    private String fromLocation;
    private String toLocation;
    private LocalDate date;
    private LocalTime time;
    private double price;
    private int seatsAvailable;
    private String status;
    private LuggageSize luggageSize;
    private Currency currency;
    private List<String> waypoints;
    private String notes;

}