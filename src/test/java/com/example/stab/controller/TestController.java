package com.example.stab.controller;

import com.example.model.dto.traveltimeservice.DadataResponseDTO;
import com.example.model.dto.traveltimeservice.TwoGisRequestDTO;
import com.example.model.dto.traveltimeservice.TwoGisResponseDTO;
import com.example.model.dto.traveltimeservice.TwoGisResponseDataDTO;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/test")
public class TestController {

    @PostMapping("/dadata")
    public List<DadataResponseDTO> testDadata(@RequestBody List<String> addresses) {
        return Collections.singletonList(new DadataResponseDTO("54.8430128", "83.0908412"));
    }

    @PostMapping("/twogis/global")
    public TwoGisResponseDTO testTwoGis(@RequestBody TwoGisRequestDTO twoGisRequestDTO) {
        LocalTime localTime = twoGisRequestDTO.getDepartureTime().toInstant().atZone(ZoneId.systemDefault()).toLocalTime();
        int max = getMax(localTime);
        return new TwoGisResponseDTO(
                twoGisRequestDTO,
                Collections.singletonList(new TwoGisResponseDataDTO(
                        String.valueOf(max))
                ), "test");
    }

    private static int getMax(LocalTime localTime) {
        int max = 60 * 60 * 10;
        if (localTime.isBefore(LocalTime.of(8, 30))) {
            max = 60 * 60 * 2;
        } else if  (localTime.isAfter(LocalTime.of(8, 30)) && localTime.isBefore(LocalTime.of(8, 45))) {
            max = 60 * 15;
        } else if  (localTime.isAfter(LocalTime.of(8, 45)) && localTime.isBefore(LocalTime.of(9, 0))) {
            max = 60 * 40;
        } else if (localTime.isAfter(LocalTime.of(9, 0)) && localTime.isBefore(LocalTime.of(9, 30))) {
            max = 60 * 30;
        } else if (localTime.isAfter(LocalTime.of(9, 30)) && localTime.isBefore(LocalTime.of(10, 0))) {
            max = 60 * 20;
        } else if (localTime.isAfter(LocalTime.of(10, 30)) && localTime.isBefore(LocalTime.of(11, 0))) {
            max = 60  * 15;
        } else if (localTime.isAfter(LocalTime.of(11, 0)) && localTime.isBefore(LocalTime.of(12, 0))) {
            max = 60 * 10;
        } else if (localTime.isAfter(LocalTime.of(17, 0)) && localTime.isBefore(LocalTime.of(17, 30))) {
            max = 60 * 1;
        } else if (localTime.isAfter(LocalTime.of(17, 30)) && localTime.isBefore(LocalTime.of(18, 0))) {
            max = 60 * 1;
        } else if (localTime.isAfter(LocalTime.of(18, 0)) && localTime.isBefore(LocalTime.of(18, 30))) {
            max = 60 * 1;
        } else if (localTime.isAfter(LocalTime.of(18, 30)) && localTime.isBefore(LocalTime.of(19, 0))) {
            max = 60 * 1;
        } else if (localTime.isAfter(LocalTime.of(19, 0)) && localTime.isBefore(LocalTime.of(19, 30))) {
            max = 60 * 1;
        } else if (localTime.isAfter(LocalTime.of(19, 30)) && localTime.isBefore(LocalTime.of(20, 0))) {
            max = 60 * 1;
        } else if (localTime.isAfter(LocalTime.of(20, 0)) && localTime.isBefore(LocalTime.of(20, 30))) {
            max = 60 * 1;
        } else if (localTime.isAfter(LocalTime.of(20, 30)) && localTime.isBefore(LocalTime.of(21, 0))) {
            max = 60 * 1;
        } else if (localTime.isAfter(LocalTime.of(21, 0)) && localTime.isBefore(LocalTime.of(21, 30))) {
            max = 60 * 1;
        } else {
            max = 60 * 60 * 10;
        }
        return max;
    }
}
