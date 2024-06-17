package com.example.service;

import com.example.config.properties.TravelTimeProperties;
import com.example.model.dto.UserDTO;
import com.example.model.dto.traveltimeservice.DadataResponseDTO;
import com.example.model.dto.traveltimeservice.RoutingPointTwoGis;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static java.util.Collections.singletonList;

@RequiredArgsConstructor
@Service
public class DadataService {
    private final RestTemplate restTemplate;
    private final TravelTimeProperties travelTimeProperties;

    public List<RoutingPointTwoGis> getRoutingPoints(UserDTO userDTO) {
        HttpHeaders headers = getHeaders();

        HttpEntity<List<String>> homeAddressEntity = new HttpEntity<>(Collections.singletonList(userDTO.getHomeAddress()), headers);
        HttpEntity<List<String>> jobAddressEntity = new HttpEntity<>(Collections.singletonList(userDTO.getJobAddress()), headers);

        List<DadataResponseDTO> listHomeAddressPointDadata = restTemplate.exchange(
                travelTimeProperties.getDadataApiUrl(),
                HttpMethod.POST,
                homeAddressEntity,
                new ParameterizedTypeReference<List<DadataResponseDTO>>() {}).getBody();
        List<DadataResponseDTO> listJobAddressPointDadata = restTemplate.exchange(
                travelTimeProperties.getDadataApiUrl(),
                HttpMethod.POST,
                jobAddressEntity,
                new ParameterizedTypeReference<List<DadataResponseDTO>>() {}).getBody();

        if (listHomeAddressPointDadata == null) {
            throw new RuntimeException("Home address response is null");
        }
        if (listJobAddressPointDadata == null) {
            throw new RuntimeException("Job address response is null");
        }

        if (listHomeAddressPointDadata.isEmpty()) {
            throw new RuntimeException("Home address is not found");
        }
        if (listJobAddressPointDadata.isEmpty()) {
            throw new RuntimeException("Job address is not found");
        }

        DadataResponseDTO homeAddressPointDadata = listHomeAddressPointDadata.get(0);
        DadataResponseDTO jobAddressPointDadata = listJobAddressPointDadata.get(0);


        RoutingPointTwoGis homeAddressPoint = new RoutingPointTwoGis(homeAddressPointDadata);
        RoutingPointTwoGis jobAddressPoint = new RoutingPointTwoGis(jobAddressPointDadata);

        return new ArrayList<>(){
            {
                add(homeAddressPoint);
                add(jobAddressPoint);
            }
        };
    }

    private HttpHeaders getHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(singletonList(MediaType.APPLICATION_JSON));
        headers.add("Authorization", "Token " + travelTimeProperties.getDadataApiKey());
        headers.add("X-Secret", travelTimeProperties.getDadataSecretKey());
        return headers;
    }
}
