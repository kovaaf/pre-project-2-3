package com.example.service;

import com.example.config.properties.TravelTimeProperties;
import com.example.model.dto.UserDTO;
import com.example.model.dto.traveltimeservice.RoutingPointTwoGis;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class TravelTimeService {
    private final TravelTimeProperties travelTimeProperties;
    private final DadataService dadataService;
    private final TwoGisService twoGisService;

    public void setDepartureTimeAndTravelTime(UserDTO userDTO) {
        List<RoutingPointTwoGis> routingPoints = dadataService.getRoutingPoints(userDTO);

        Map<LocalTime, Duration> travelTimeByDepartureTime = getTravelTimeByDepartureTime(routingPoints);
        Map.Entry<LocalTime, Duration> minimumTravelTimeByDepartureTime = findMinimumTravelTime(travelTimeByDepartureTime);

        LocalTime departureTime = minimumTravelTimeByDepartureTime.getKey();
        userDTO.setDepartureTime(departureTime);
        userDTO.setTravelTime(convertDurationToLocalTime(minimumTravelTimeByDepartureTime.getValue()));
    }

    private Map<LocalTime, Duration> getTravelTimeByDepartureTime(List<RoutingPointTwoGis> routingPoints) {
        Map<LocalTime, Duration> travelTimeByDepartureTime = new HashMap<>();
        for (LocalTime departureTime = getMinimumDepartureTime(); departureTime.isBefore(getMaximumArrivalTime()); departureTime = departureTime.plusMinutes(travelTimeProperties.getSamplingStep())) {
            travelTimeByDepartureTime.put(departureTime, twoGisService.getTravelTimeForDepartureTime(routingPoints, departureTime));
        }

        travelTimeByDepartureTime.entrySet().removeIf(this::isAfterMaximumArrivalTime);
        travelTimeByDepartureTime.entrySet().removeIf(this::isBeforeMinimumArrivalTime);

        return travelTimeByDepartureTime;
    }

    private boolean isBeforeMinimumArrivalTime(Map.Entry<LocalTime, Duration> e) {
        return e.getKey().plusSeconds(e.getValue().getSeconds()).isBefore(getMinimumArrivalTime());
    }

    private boolean isAfterMaximumArrivalTime(Map.Entry<LocalTime, Duration> e) {
        return e.getKey().plusSeconds(e.getValue().getSeconds()).isAfter(getMaximumArrivalTime());
    }

    private LocalTime getMinimumArrivalTime() {
        return LocalTime.of(travelTimeProperties.getMinArrivalHours(), travelTimeProperties.getMinArrivalMinutes());
    }

    private LocalTime getMaximumArrivalTime() {
        return LocalTime.of(travelTimeProperties.getMaxArrivalHours(), travelTimeProperties.getMaxArrivalMinutes());
    }

    private LocalTime getMinimumDepartureTime() {
        return LocalTime.of(travelTimeProperties.getMinArrivalHours(), travelTimeProperties.getMinArrivalMinutes()).minusMinutes(travelTimeProperties.getAmountOfTimeCheckedBeforeMinArrival());
    }

    private Map.Entry<LocalTime, Duration> findMinimumTravelTime(Map<LocalTime, Duration> travelTimeByDepartureTime) {
        Map.Entry<LocalTime, Duration> minimumTravelTimeByDepartureTime = null;
        for (Map.Entry<LocalTime, Duration> entry : travelTimeByDepartureTime.entrySet()) {
            if (minimumTravelTimeByDepartureTime == null) {
                minimumTravelTimeByDepartureTime = entry;
            } else if (entry.getValue().compareTo(minimumTravelTimeByDepartureTime.getValue()) < 0) {
                minimumTravelTimeByDepartureTime = entry;
            } else if (entry.getValue().compareTo(minimumTravelTimeByDepartureTime.getValue()) == 0 && entry.getKey().isBefore(minimumTravelTimeByDepartureTime.getKey())) {
                minimumTravelTimeByDepartureTime = entry;
            }
        }

        return minimumTravelTimeByDepartureTime;
    }

    private LocalTime convertDurationToLocalTime(Duration duration) {
        long seconds = duration.getSeconds();
        int hours = Long.valueOf(seconds / 3600).intValue();
        if (hours > 23) {
            throw new RuntimeException("Hours is greater than 23");
        }
        return LocalTime.of(hours, Long.valueOf(seconds % 3600 / 60).intValue(), Long.valueOf(seconds % 60).intValue());
    }
}
