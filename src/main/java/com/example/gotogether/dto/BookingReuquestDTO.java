package com.example.gotogether.dto;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingReuquestDTO {
    private Long rideId;
    private int numberOfSeats;
}
