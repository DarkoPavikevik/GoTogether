package com.example.gotogether.model;

import com.example.gotogether.enums.Currency;
import com.example.gotogether.enums.LuggageSize;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Ride {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "driver_id")
    @JsonIgnore
    private User driver;

    @ManyToOne
    @JoinColumn(name = "vehicle_id")
    private Vehicle vehicle;

    private String fromLocation;
    private String toLocation;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;

    private LocalTime time;
    private double price;

    @Enumerated(EnumType.STRING)
    private Currency currency;

    private int seatsAvailable;
    private String status;
    private LuggageSize luggageSize;

    @ElementCollection
    private List<String> waypoints;

    private String notes;

    @OneToMany(mappedBy = "ride")
    @JsonIgnore
    private List<Booking> bookings;

    public Ride(Long id) {
        this.id = id;
    }


    public void setUser(User user) {
        this.driver = user;
    }
}
