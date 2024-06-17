package com.example.model.dto.traveltimeservice;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
public class TwoGisResponseDTO {
    private TwoGisRequestDTO query;
    private List<TwoGisResponseDataDTO> result;
    private String type;
}
