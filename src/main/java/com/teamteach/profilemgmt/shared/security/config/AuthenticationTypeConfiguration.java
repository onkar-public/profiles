package com.teamteach.profilemgmt.shared.security.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@Getter
public class AuthenticationTypeConfiguration {

    @Value("${app.security.authentication.type}")
    private AuthenticationTypes authenticationType;

    @Bean(name = "bcryptEncoder")
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
