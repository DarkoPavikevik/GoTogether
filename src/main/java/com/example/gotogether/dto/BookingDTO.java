package com.example.gotogether.dto;

import com.example.gotogether.enums.BookingStatus;
import jakarta.persistence.Column;
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
    private String username;
    private String phoneNumber;
    private Long rideId;
    private BookingStatus status; // e.g., "PENDING", "CONFIRMED", "REJECTED"
    private String pickupLocation;
    private String dropoffLocation;
    private String note;
    private boolean emailSent;
}

