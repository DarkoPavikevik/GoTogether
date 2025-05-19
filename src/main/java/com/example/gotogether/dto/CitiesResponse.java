package com.example.gotogether.dto;


import lombok.Data;

import java.util.List;

@Data
public class CitiesResponse {

    private boolean error;
    private String message;
    private List<String> data;
}

