package com.example.stab;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class TestAppRunner {
    public static void main(String[] args) {
        SpringApplication.run(TestAppRunner.class, args);
    }
}
