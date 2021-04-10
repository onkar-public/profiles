package com.teamteach.profilemgmt;

import com.teamteach.commons.security.jwt.config.JwtConfig;
import com.teamteach.commons.security.jwt.config.KeyConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({JwtConfig.class, KeyConfig.class})
public class ProfileMgmtApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProfileMgmtApplication.class, args);
	}

}
