package com.teamteach.profilemgmt.shared.security.jwt;

import com.google.common.base.Strings;
import com.teamteach.profilemgmt.shared.security.jwt.config.KeyConfig;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.security.Key;

@Slf4j
@Service
public class SecretKeyProvider implements ISecretKeyProvider {

    @Autowired
    Environment environment;

    @Autowired
    ApplicationContext applicationContext;

    @Autowired
    KeyConfig keyConfig;

    Key secretKey;

    @PostConstruct
    void init() {
        String signingSecret = null;
        boolean shared = false;
        if( keyConfig.getKeytype() != null )
               shared = keyConfig.getKeytype().equalsIgnoreCase("shared");
        if (keyConfig.isUseenv()) {
            signingSecret = environment.getProperty(keyConfig.getKeyenvvar(), "");
            if (Strings.isNullOrEmpty(signingSecret)) {
                log.error("The Secret Key is not found in the Env Variable ");
                ((ConfigurableApplicationContext) applicationContext).close();
            }
        } else {
            signingSecret = keyConfig.getSigningsecret();
        }

        if (shared) {
            secretKey = Keys.hmacShaKeyFor(signingSecret.getBytes());
        } else {
            secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS512);
        }
    }

    @Override
    public Key getKey() {
       return secretKey;
    }
}
