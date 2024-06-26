package org.company.controller;

import org.company.model.dto.DadataResponseDTO;
import org.company.model.dto.TwoGisRequestDTO;
import org.company.model.dto.TwoGisResponseDTO;
import org.company.model.dto.TwoGisResponseDataDTO;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/test")
public class TestController {
    private static final int MAX_TRAVEL_TIME = 60 * 60 * 23 + 59 * 60 + 59;
    private static final int ONE_HOUR = 60 * 60;
    private static final int ONE_MINUTE = 60;
    private static final int ONE_SECOND = 1;

    @PostMapping("/dadata")
    public List<DadataResponseDTO> testDadata(@RequestBody List<String> addresses) {
        System.out.println("testDadata is called with: " + addresses);
        return Collections.singletonList(new DadataResponseDTO("54.8430128", "83.0908412"));
    }

    @PostMapping("/twogis/global")
    public TwoGisResponseDTO testTwoGis(@RequestBody TwoGisRequestDTO twoGisRequestDTO) {
        Date departureTime = twoGisRequestDTO.getDepartureTime();
        LocalTime localTime = departureTime.toInstant().atZone(ZoneId.of("UTC")).toLocalTime();
        int travelTime = getTravelTime(localTime);
        System.out.println("Время отправления: " + localTime + " Время в пути: " + convertDurationToLocalTime(travelTime));
        return new TwoGisResponseDTO(
                twoGisRequestDTO,
                Collections.singletonList(new TwoGisResponseDataDTO(
                        String.valueOf(travelTime))
                ), "test");
    }

    private static int getTravelTime(LocalTime localTime) {
        int travelTime = MAX_TRAVEL_TIME;
        if (localTime.isBefore(LocalTime.of(8, 30))) {
            travelTime = ONE_HOUR;
        } else if  (localTime.isAfter(LocalTime.of(8, 30).minusSeconds(ONE_SECOND)) && localTime.isBefore(LocalTime.of(8, 45))) {
            travelTime = ONE_MINUTE * 50;
        } else if  (localTime.isAfter(LocalTime.of(8, 45).minusSeconds(ONE_SECOND)) && localTime.isBefore(LocalTime.of(9, 0))) {
            travelTime = ONE_MINUTE * 40;
        } else if (localTime.isAfter(LocalTime.of(9, 0).minusSeconds(ONE_SECOND)) && localTime.isBefore(LocalTime.of(9, 30))) {
            travelTime = ONE_MINUTE * 30;
        } else if (localTime.isAfter(LocalTime.of(8, 30).minusSeconds(ONE_SECOND)) && localTime.isBefore(LocalTime.of(10, 0))) {
            travelTime = ONE_MINUTE * 20;
        } else if (localTime.isAfter(LocalTime.of(10, 0).minusSeconds(ONE_SECOND)) && localTime.isBefore(LocalTime.of(11, 0))) {
            travelTime = ONE_MINUTE * 15;
        } else if (localTime.isAfter(LocalTime.of(11, 9).minusSeconds(ONE_SECOND)) && localTime.isBefore(LocalTime.of(12, 0))) {
            travelTime = ONE_MINUTE * 10;
        } else if (localTime.isAfter(LocalTime.of(17, 0).minusSeconds(ONE_SECOND)) && localTime.isBefore(LocalTime.of(17, 30))) {
            travelTime = ONE_HOUR;
        } else if (localTime.isAfter(LocalTime.of(17, 30).minusSeconds(ONE_SECOND)) && localTime.isBefore(LocalTime.of(18, 0))) {
            travelTime = ONE_HOUR;
        } else if (localTime.isAfter(LocalTime.of(18, 0).minusSeconds(ONE_SECOND)) && localTime.isBefore(LocalTime.of(18, 30))) {
            travelTime = ONE_HOUR;
        } else if (localTime.isAfter(LocalTime.of(18, 30).minusSeconds(ONE_SECOND)) && localTime.isBefore(LocalTime.of(19, 0))) {
            travelTime = ONE_HOUR;
        } else if (localTime.isAfter(LocalTime.of(19, 0).minusSeconds(ONE_SECOND)) && localTime.isBefore(LocalTime.of(19, 30))) {
            travelTime = ONE_HOUR;
        } else if (localTime.isAfter(LocalTime.of(19, 30).minusSeconds(ONE_SECOND)) && localTime.isBefore(LocalTime.of(20, 0))) {
            travelTime = ONE_HOUR;
        } else if (localTime.isAfter(LocalTime.of(20, 0).minusSeconds(ONE_SECOND)) && localTime.isBefore(LocalTime.of(20, 30))) {
            travelTime = ONE_HOUR;
        } else if (localTime.isAfter(LocalTime.of(20, 30).minusSeconds(ONE_SECOND)) && localTime.isBefore(LocalTime.of(21, 0))) {
            travelTime = ONE_HOUR;
        } else if (localTime.isAfter(LocalTime.of(21, 0).minusSeconds(ONE_SECOND)) && localTime.isBefore(LocalTime.of(21, 30))) {
            travelTime = ONE_HOUR;
        }
        return travelTime;
    }

    private LocalTime convertDurationToLocalTime(int seconds) {
        return LocalTime.of(Long.valueOf(seconds / 3600).intValue(), Long.valueOf(seconds % 3600 / 60).intValue(), Long.valueOf(seconds % 60).intValue());
    }
}
