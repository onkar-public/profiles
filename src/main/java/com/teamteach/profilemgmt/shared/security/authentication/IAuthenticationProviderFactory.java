package com.teamteach.profilemgmt.shared.security.authentication;

import org.springframework.security.authentication.AuthenticationProvider;

public interface IAuthenticationProviderFactory {
    AuthenticationProvider getConfiguredProvider();
}
