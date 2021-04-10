package com.teamteach.profilemgmt.shared.security.jwt.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties("app.security.key")
public class KeyConfig {
    private String keytype;
    private String keyenvvar;
    private boolean useenv;
    private String signingsecret;
}
