package com.example.gotogether.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SupportRequestDTO {
    String fullName;
    String email;
    String subject;
    String message;
    String category;

}
