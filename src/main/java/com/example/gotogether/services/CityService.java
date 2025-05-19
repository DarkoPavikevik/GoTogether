package com.example.gotogether.services;


import com.example.gotogether.dto.CitiesResponse;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
public class CityService {

    private final RestTemplate restTemplate;

    public CityService(RestTemplateBuilder builder) {
        this.restTemplate = builder.build();
    }


    public List<String> getCitiesForCountry(String country) {
        String url = "https://countriesnow.space/api/v0.1/countries/cities";
        Map<String, String> request = Map.of("country", country);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, String>> entity = new HttpEntity<>(request, headers);

        ResponseEntity<CitiesResponse> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                entity,
                CitiesResponse.class
        );

        return response.getBody().getData();
    }



}
