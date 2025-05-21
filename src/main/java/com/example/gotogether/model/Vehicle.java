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


    @ManyToOne
    @JsonIgnore
    private User user;
    private String brand;
    private String model;
    private Integer year;
    private String plateNumber;
    private Integer seats;
    private String color;


    public void setUser(User user) {
        this.user = user;
    }


}

