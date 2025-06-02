package com.example.gotogether.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Vehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @OneToOne
    @JsonIgnore
    private User user;
    private String brand;
    private String model;
    private Integer year;
    private String plateNumber;
    private String picture;
    private Integer seats;
    private String color;
    private Boolean airCondition = false;
    private Boolean usbCharging = false;
    private Boolean music = false;
    private Boolean comfortableSeats = false;


    public void setUser(User user) {
        this.user = user;
    }


}

