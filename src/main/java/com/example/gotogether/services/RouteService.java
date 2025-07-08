package com.example.gotogether.services;


import lombok.RequiredArgsConstructor;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class RouteService {



    private final String API_KEY = "5b3ce3597851110001cf62488d8bf5fbf5134087a250246d98fe102c";
    private final String URL = "https://api.openrouteservice.org/v2/directions/driving-car";


    private final Map<String, List<Double>> cachedCoordinates = new HashMap<>();

    private List<List<Double>> getCoordinatesForCities(List<String> cities) {
        String geocodeUrl = "https://api.openrouteservice.org/geocode/search";
        RestTemplate restTemplate = new RestTemplate();
        List<List<Double>> coordinates = new ArrayList<>();

        for (String city : cities) {
            if (cachedCoordinates.containsKey(city)) {
                coordinates.add(cachedCoordinates.get(city));
                continue;
            }

            String url = geocodeUrl + "?api_key=" + API_KEY + "&text=" + city + "&boundary.country=MK";
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
            JSONObject json = new JSONObject(response.getBody());
            JSONArray features = json.getJSONArray("features");

            if (features.length() == 0) {
                throw new RuntimeException("Could not find coordinates for city: " + city);
            }

            JSONArray coordArray = features.getJSONObject(0)
                    .getJSONObject("geometry")
                    .getJSONArray("coordinates");

            List<Double> coord = Arrays.asList(coordArray.getDouble(0), coordArray.getDouble(1));
            cachedCoordinates.put(city, coord);
            coordinates.add(coord);
        }

        return coordinates;
    }


    public List<String> getEstimatedArrivalTimes(List<String> cities, LocalTime startTime) {
        List<List<Double>> coordinates = getCoordinatesForCities(cities);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", API_KEY);
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> body = new HashMap<>();
        body.put("coordinates", coordinates);
        body.put("radiuses", Collections.nCopies(coordinates.size(), 1000));

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.postForEntity(URL, request, String.class);

        JSONObject json = new JSONObject(response.getBody());
        JSONArray segments = json.getJSONArray("routes")
                .getJSONObject(0)
                .getJSONArray("segments");

        List<String> arrivalTimes = new ArrayList<>();
        arrivalTimes.add(cities.get(0) + " (Start): " + startTime);

        double totalDuration = 0;
        LocalTime lastArrivalTime = startTime;

        for (int i = 0; i < segments.length(); i++) {
            double duration = segments.getJSONObject(i).getDouble("duration"); // in seconds
            totalDuration += duration;
            lastArrivalTime = startTime.plusSeconds((long) totalDuration);
            arrivalTimes.add(cities.get(i + 1) + " (Arrival): " + lastArrivalTime);
        }

        Duration durationBetween = Duration.between(startTime, lastArrivalTime);
        if (durationBetween.isNegative()) {
            durationBetween = durationBetween.plusHours(24); // Handle wrap around midnight
        }

        long hours = durationBetween.toHours();
        long minutes = durationBetween.toMinutesPart();

        String tripDuration = String.format("Total trip duration: %02d:%02d", hours, minutes);
        arrivalTimes.add(tripDuration);

        return arrivalTimes;
    }


}
