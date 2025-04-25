package com.example.gotogether.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingDTO {

    private Long id;
    private Long userId;
    private Long rideId;
    private int numberOfSeats;
    private String status; // e.g., "PENDING", "CONFIRMED", "REJECTED"
    private boolean emailSent;
}

