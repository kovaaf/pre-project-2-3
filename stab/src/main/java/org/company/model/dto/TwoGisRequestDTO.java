package org.company.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@RequiredArgsConstructor
@Getter
@Setter
public class TwoGisRequestDTO {
    @JsonProperty("points")
    private List<RoutingPointTwoGis> routingPoints;
    @JsonProperty("route_mode")
    private String routeMode;
    @JsonProperty("traffic_mode")
    private String trafficMode;
    private String output;
    @JsonProperty("utc")
    private Date departureTime;
}
