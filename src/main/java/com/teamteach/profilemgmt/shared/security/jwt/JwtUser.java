package com.teamteach.profilemgmt.shared.security.jwt;

import lombok.Data;

import java.util.List;

@Data
public class JwtUser {
    private String principal;
    private String fullname;
    private Object data;
    private List<String> authorities;
    private List<String> groups;

    public String getPrincipal() {
        return principal != null ? principal : "Anonymous";
    }

}
