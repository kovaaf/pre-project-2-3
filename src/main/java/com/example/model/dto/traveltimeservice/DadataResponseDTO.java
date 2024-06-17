package com.example.model.dto.traveltimeservice;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
public class DadataResponseDTO {
        @JsonProperty("geo_lat")
        private String latitude;
        @JsonProperty("geo_lon")
        private String longitude;
}