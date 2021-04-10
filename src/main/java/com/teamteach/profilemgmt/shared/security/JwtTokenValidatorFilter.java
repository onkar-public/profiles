package com.teamteach.profilemgmt.shared.security;


import com.teamteach.profilemgmt.shared.security.authentication.AppAuthenticationToken;
import com.teamteach.profilemgmt.shared.security.jwt.JwtOperationsWrapperSvc;
import com.teamteach.profilemgmt.shared.security.jwt.JwtUser;
import com.teamteach.profilemgmt.shared.utils.AppSecurityConstants;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.stream.Collectors;


public class JwtTokenValidatorFilter extends BasicAuthenticationFilter {

    private final JwtOperationsWrapperSvc jwtOperationsWrapperSvc;

    public JwtTokenValidatorFilter(AuthenticationManager authenticationManager, JwtOperationsWrapperSvc jwtOperationsUtils) {
        super(authenticationManager);
        this.jwtOperationsWrapperSvc = jwtOperationsUtils;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String header = request.getHeader(AppSecurityConstants.AUTHORIZATION_HEADER);
        String url = request.getRequestURI();
        if (header == null || !header.startsWith(AppSecurityConstants.AUTHORIZATION_HEADER_TOKEN_PREFIX)) {
            chain.doFilter(request, response);
            return;
        }
        String token = header.replace(AppSecurityConstants.AUTHORIZATION_HEADER_TOKEN_PREFIX, "").trim();
        JwtUser jwtUser = jwtOperationsWrapperSvc.validateToken(token);
        if (jwtUser != null) {
            Collection<? extends SimpleGrantedAuthority> authorities =
                    jwtUser.getAuthorities().stream().map(a -> new SimpleGrantedAuthority(a)).collect(Collectors.toList());
            Authentication authentication
                    = new AppAuthenticationToken(jwtUser,
                    null,
                    authorities,
                    token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        chain.doFilter(request,response);
    }
}