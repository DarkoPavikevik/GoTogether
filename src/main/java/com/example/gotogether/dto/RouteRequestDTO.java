package com.example.gotogether.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RouteRequestDTO {
    private String fromLocation;
    private String toLocation;
    private List<String> waypoints;
    private LocalTime startTime;
}
