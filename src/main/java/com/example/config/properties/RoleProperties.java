package com.example.config.properties;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@ConfigurationProperties(prefix = "roles")
public class RoleProperties {
    @Value("${roles.admin}")
    private String admin;
    @Value("${roles.user}")
    private String user;
}
