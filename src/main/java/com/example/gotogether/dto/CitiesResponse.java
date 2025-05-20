package com.example.gotogether.dto;


import lombok.Data;

import java.util.List;

@Data
public class CitiesResponse {

    private boolean error;
    private String msg;
    private List<String> data;
}

