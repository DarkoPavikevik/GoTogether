package com.example.gotogether.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
@Builder
public class RideEstimateResponseDTO {
    private List<String> estimatedArrivalTimes;
}
