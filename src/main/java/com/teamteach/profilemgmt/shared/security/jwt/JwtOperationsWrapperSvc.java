package com.teamteach.profilemgmt.shared.security.jwt;

import com.teamteach.profilemgmt.shared.security.jwt.config.JwtConfig;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class JwtOperationsWrapperSvc {

    @Autowired
    JwtConfig jwtConfig;

    @Autowired
    ISecretKeyProvider secretKeyProvider;

    public static final  String AUTHORITIES = "authorities";

    public String generateToken(String subject, Collection authorities, Object payload) {
        var tokenModel = JwtTokenGenModel.builder()
                .subjectId(subject)
                .subjectName(subject)
                .authorities(authorities)
                .build();

        return generateToken(tokenModel);
    }

    public String generateToken(JwtTokenGenModel jwtTokenGenModel) {

        Calendar date = Calendar.getInstance();
        long t = date.getTimeInMillis();
        Date expirationDate = new Date(t + (jwtConfig.getExpirationmins() * 60000));

        JwtBuilder jwtBuilder = Jwts.builder()
                .setSubject(jwtTokenGenModel.getSubjectName())
                .setId(jwtTokenGenModel.getSubjectId())
                .setAudience(jwtConfig.getAudience())
                .setIssuer(jwtConfig.getIssuer())
                .setIssuedAt(new Date())
                .setExpiration(expirationDate)
                .claim(AUTHORITIES, jwtTokenGenModel.getAuthorities())
                .signWith(secretKeyProvider.getKey());
        jwtBuilder.addClaims(jwtTokenGenModel.getOtherClaims());
        String token = jwtBuilder.compact();
        assert (token.length() > 0);
        return token;
    }

    public JwtUser validateToken(String token) {
        Jws<Claims> jws;
        try {
            jws = Jwts.parserBuilder()
                    .setSigningKey(secretKeyProvider.getKey())
                    .build()
                    .parseClaimsJws(token);
            JwtUser user = new JwtUser();
            Claims body = jws.getBody();

            user.setData(jws.getHeader().get("t"));
            user.setPrincipal(jws.getBody().getId());
            user.setFullname(jws.getBody().getSubject());
            List<Map<String,String>> authorities = (List<Map<String,String>>) body.get(AUTHORITIES);
            if (authorities != null)
                user.setAuthorities(authorities.stream().map( a -> a.get("authority")).collect(Collectors.toList()));
            return user;
        } catch (JwtException ex) {
            return null;
        }
    }
}
