package com.example.gotogether.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserInfoDTO {
    private Long id;
    private String name;
    private String avatar;
    private String vehicle;
    private String rating;
    private Integer numberOfRides;
    private List<ReviewDTO> reviews;
}