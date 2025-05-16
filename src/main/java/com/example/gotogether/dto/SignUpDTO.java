package com.example.gotogether.dto;

import lombok.*;

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
