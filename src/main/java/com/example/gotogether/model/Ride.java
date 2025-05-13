package com.example.gotogether.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Ride {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "driver_id")
    private User driver;

    @ManyToOne
    @JoinColumn(name = "vehicle_id")
    private Vehicle vehicle;

    private String fromLocation;
    private String toLocation;
    private LocalDate date;
    private LocalTime time;
    private double price;
    private int seatsAvailable;
    private String status;
    private String baggageInfo;// e.g., "OPEN", "FULL", "CANCELLED"

    @ElementCollection
    private List<String> waypoints;

    private String notes;

    @OneToMany(mappedBy = "ride")
    private List<Booking> bookings;

    public Ride(Long id) {
        this.id = id;
    }
}
