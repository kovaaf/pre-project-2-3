package com.example.model.dto.traveltimeservice;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class RoutingPointTwoGis {
    private String type;
    @JsonProperty("x")
    private String longitude;
    @JsonProperty("y")
    private String latitude;

    public RoutingPointTwoGis(DadataResponseDTO dadataResponseDTO) {
        this.longitude = dadataResponseDTO.getLongitude();
        this.latitude = dadataResponseDTO.getLatitude();
    }
}
