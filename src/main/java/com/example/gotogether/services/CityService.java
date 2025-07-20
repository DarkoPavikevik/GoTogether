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
        if (country.equalsIgnoreCase("macedonia")) {
            return List.of(
                    "Skopje", "Bitola", "Kumanovo", "Prilep", "Tetovo", "Veles", "Ohrid", "Štip", "Gostivar", "Strumica",
                    "Kočani", "Kavadarci", "Radoviš", "Negotino", "Struga", "Gevgelija", "Debar", "Kriva Palanka", "Berovo",
                    "Vinica", "Kičevo", "Resen", "Delčevo", "Bogdanci", "Demir Kapija", "Demir Hisar", "Makedonska Kamenica",
                    "Krusevo", "Kratovo", "Valandovo", "Probistip", "Sveti Nikole", "Pehčevo", "Makedonski Brod", "Drugovo",
                    "Zelenikovo", "Bosilovo", "Konče", "Rankovce", "Rostuša", "Tearce", "Jegunovce", "Brvenica", "Lozovo",
                    "Novo Selo", "Staro Nagoričane", "Vasilevo", "Vrapčište", "Zrnovci", "Lipkovo", "Čaška", "Čučer-Sandevo",
                    "Plasnica", "Dolneni", "Studeničani", "Aracinovo", "Bogovinje", "Vevčani", "Centar Župa"
            );
        }
        return List.of();
    }
    /*public List<String> getCitiesForCountry(String country) {
        String url = "https://countriesnow.space/api/v0.1/countries/cities/q?country=" + country;

        // No request body needed, it's a GET request now
        ResponseEntity<CitiesResponse> response = restTemplate.getForEntity(url, CitiesResponse.class);
        System.out.println("Status code: " + response.getStatusCode());
        System.out.println("Body: " + response.getBody());

        return response.getBody() != null ? response.getBody().getData() : List.of();
    }*/



}
