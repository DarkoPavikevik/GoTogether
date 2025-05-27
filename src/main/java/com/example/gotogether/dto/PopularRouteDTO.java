package com.example.gotogether.dto;

import lombok.*;



@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PopularRouteDTO {

    private String fromLocation;
    private String toLocation;
    private Long rideCount;
}
