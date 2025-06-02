package com.example.gotogether.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDate;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO {

    private Long id;
    private String bio;
    private String name;
    private String email;
    private String role;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate created;
    private Double rating;
    private String username;
    private String phoneNumber;
    private String profilePicture;
    private String password;
    private Boolean smoking;
    private Boolean pets;
    private Boolean music;
    private Boolean talking;
    private VehicleDTO vehicle;
    private ReviewDTO review;
}
