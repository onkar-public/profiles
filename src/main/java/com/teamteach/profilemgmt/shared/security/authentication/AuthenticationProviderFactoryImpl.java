package com.teamteach.profilemgmt.shared.security.authentication;

import com.teamteach.profilemgmt.shared.security.config.AuthenticationTypeConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationProviderFactoryImpl implements IAuthenticationProviderFactory {

    @Autowired
    AuthenticationTypeConfiguration authenticationTypeConfiguration;

    @Autowired
    BCryptPasswordEncoder bcryptEncoder;

    @Autowired
    UserDetailsService userDetailsService;

    @Override
    public AuthenticationProvider getConfiguredProvider() {

        switch (authenticationTypeConfiguration.getAuthenticationType()) {
            case LDAP:
                return getLdapAuthenticationProvider();
            default:
                return getDaoAuthenticationProvider();
        }
    }

    private AuthenticationProvider getDaoAuthenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setPasswordEncoder(bcryptEncoder);
        daoAuthenticationProvider.setUserDetailsService(userDetailsService);
        return daoAuthenticationProvider;
    }

    private AuthenticationProvider getLdapAuthenticationProvider() {
        return null;
    }
}
