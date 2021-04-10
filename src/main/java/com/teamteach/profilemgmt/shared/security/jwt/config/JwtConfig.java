package com.teamteach.profilemgmt.shared.security.jwt.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties("app.security.jwt")
public class JwtConfig {
    private String audience;
    private String issuer;
    private long expirationmins;
    private long expirationdays;
    private String keytype;
}

