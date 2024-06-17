package com.example.config.properties;


import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "travel-time")
public class TravelTimeProperties {
    @Value("${travel-time.dadataApiUrl}")
    private String dadataApiUrl;
    @Value("${travel-time.dadataApiKey}")
    private String dadataApiKey;
    @Value("${travel-time.dadataSecretKey}")
    private String dadataSecretKey;
    @Value("${travel-time.twoGisApiUrl}")
    private String twoGisApiUrl;
    @Value("${travel-time.twoGisApiKey}")
    private String twoGisApiKey;
    @Value("${travel-time.samplingStep}")
    private long samplingStep;
    @Value("${travel-time.workingHours}")
    private long workingHours;
    @Value("${travel-time.amountOfTimeCheckedBeforeMinArrival}")
    private long amountOfTimeCheckedBeforeMinArrival;
    @Value("${travel-time.minArrivalHours}")
    private int minArrivalHours;
    @Value("${travel-time.minArrivalMinutes}")
    private int minArrivalMinutes;
    @Value("${travel-time.maxArrivalHours}")
    private int maxArrivalHours;
    @Value("${travel-time.maxArrivalMinutes}")
    private int maxArrivalMinutes;
}
