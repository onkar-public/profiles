package com.teamteach.profilemgmt.shared.security.jwt;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Collection;
import java.util.Map;

@Getter
@Builder
public class JwtTokenGenModel {
    String subjectId;
    String subjectName;
    Collection authorities;
    @Setter
    Map<String,Object> otherClaims;
}
