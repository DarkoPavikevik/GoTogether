package com.example.gotogether.model;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User user;

    @ManyToOne
    private Ride ride;

    private int numberOfSeats;

    private String status; // e.g., "PENDING", "CONFIRMED", "REJECTED"

    private boolean emailSent;


}