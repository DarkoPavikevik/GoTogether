package com.example.gotogether.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Vehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne
    private User user;
    private String brand;
    private String model;
    private String plateNumber;
    private int seats;
    private String color;


    public void setUser(User user) {
        this.user = user;
    }


}

