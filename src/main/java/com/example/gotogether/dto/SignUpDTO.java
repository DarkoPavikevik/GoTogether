package com.example.gotogether.dto;

import lombok.*;

import java.time.LocalDate;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SignUpDTO {

    private Long id;
    private String username;
    private String email;
    private String phoneNumber;
    private String password;
    private String confirmPassword;



}
