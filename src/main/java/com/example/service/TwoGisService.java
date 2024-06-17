package com.example.service;

import com.example.config.properties.TravelTimeProperties;
import com.example.model.dto.traveltimeservice.RoutingPointTwoGis;
import com.example.model.dto.traveltimeservice.TwoGisRequestDTO;
import com.example.model.dto.traveltimeservice.TwoGisResponseDTO;
import com.example.model.dto.traveltimeservice.TwoGisResponseDataDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@RequiredArgsConstructor
@Service
public class TwoGisService {
    private final RestTemplate restTemplate;
    private final TravelTimeProperties travelTimeProperties;

    public Duration getTravelTimeForDepartureTime(List<RoutingPointTwoGis> routingPoints, LocalTime departureTime) {
        HttpHeaders headers = getHeaders();

        TwoGisRequestDTO twoGisRequestDTO = createTwoGisRequestDTO(routingPoints, departureTime);

        HttpEntity<TwoGisRequestDTO> requestEntity = new HttpEntity<>(twoGisRequestDTO, headers);
        TwoGisResponseDTO morningDepartureTime = getMorningDepartureTime(requestEntity);
        TwoGisResponseDTO eveningDepartureTime = getEveningDepartureTime(routingPoints, departureTime, twoGisRequestDTO, headers);

        Duration morningTravelTime = convertTwoGisResponseToDuration(morningDepartureTime);
        Duration eveningTravelTime = convertTwoGisResponseToDuration(eveningDepartureTime);

        return morningTravelTime.plus(eveningTravelTime);
    }

    private static HttpHeaders getHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }

    private static Duration convertTwoGisResponseToDuration(TwoGisResponseDTO morningDepartureTime) {
        TwoGisResponseDataDTO morningDepartureTimeResult = morningDepartureTime.getResult().get(0);
        return Duration.ofSeconds(Long.parseLong(morningDepartureTimeResult.getTravelTime()));
    }

    private TwoGisResponseDTO getEveningDepartureTime(List<RoutingPointTwoGis> routingPoints, LocalTime departureTime, TwoGisRequestDTO twoGisRequestDTO, HttpHeaders headers) {
        HttpEntity<TwoGisRequestDTO> requestEntity;
        twoGisRequestDTO.setDepartureTime(convertLocalTimeToDate(departureTime.plusHours(travelTimeProperties.getWorkingHours())));
        Collections.swap(routingPoints, 0, 1);
        twoGisRequestDTO.setRoutingPoints(routingPoints);
        requestEntity = new HttpEntity<>(twoGisRequestDTO, headers);
        TwoGisResponseDTO eveningDepartureTime = restTemplate.postForObject(travelTimeProperties.getTwoGisApiUrl() + travelTimeProperties.getTwoGisApiKey(), requestEntity, TwoGisResponseDTO.class);
        if (eveningDepartureTime == null) {
            throw new RuntimeException("Two gis response for evening departure is null");
        }
        return eveningDepartureTime;
    }

    private TwoGisResponseDTO getMorningDepartureTime(HttpEntity<TwoGisRequestDTO> requestEntity) {
        TwoGisResponseDTO morningDepartureTime = restTemplate.postForObject(travelTimeProperties.getTwoGisApiUrl() + travelTimeProperties.getTwoGisApiKey(), requestEntity, TwoGisResponseDTO.class);
        if (morningDepartureTime == null) {
            throw new RuntimeException("Two gis response for morning departure is null");
        }
        return morningDepartureTime;
    }

    private TwoGisRequestDTO createTwoGisRequestDTO(List<RoutingPointTwoGis> routingPoints, LocalTime departureTime) {
        TwoGisRequestDTO twoGisRequestDTO = new TwoGisRequestDTO();
        twoGisRequestDTO.setRouteMode("fastest");
        twoGisRequestDTO.setTrafficMode("statistics");
        twoGisRequestDTO.setOutput("summary");
        routingPoints.forEach(p -> p.setType("stop"));
        twoGisRequestDTO.setRoutingPoints(routingPoints);
        twoGisRequestDTO.setDepartureTime(convertLocalTimeToDate(departureTime));
        return twoGisRequestDTO;
    }

    private Date convertLocalTimeToDate(LocalTime localTime) {
        return Date.from(localTime.atDate(LocalDate.now()).atZone(ZoneId.systemDefault()).toInstant());
    }
}
