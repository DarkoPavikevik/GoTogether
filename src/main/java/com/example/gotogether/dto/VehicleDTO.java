package com.example.gotogether.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VehicleDTO {


    private Long id;

    private Long userId;

    private String brand;
    private String model;
    private String picture;
    private String plateNumber;
    private Integer seats;
    private Integer year ;
    private String color;
    private Boolean airCondition;
    private Boolean usbCharging;
    private Boolean music;
    private Boolean comfortableSeats;

}
