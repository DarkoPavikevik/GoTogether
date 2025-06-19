package com.example.gotogether.dto;

import com.example.gotogether.enums.BookingStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PassengerBookingDTO {
    private UserInfoDTO user;
    private int numberOfSeats;
    private BookingStatus status;
}
