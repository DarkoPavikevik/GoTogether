package com.example.gotogether.model;


import com.example.gotogether.enums.BookingStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JsonIgnore
    private User user;

    @ManyToOne
    @JsonIgnore
    private Ride ride;

    @Column(nullable = false)
    private String pickupLocation;

    @Column(nullable = false)
    private String dropoffLocation;

    private Double pickupLat;
    private Double pickupLng;

    private Double dropoffLat;
    private Double dropoffLng;

    private BookingStatus status; // e.g., "PENDING", "CONFIRMED", "REJECTED"
    private String note;

    private boolean emailSent;


}