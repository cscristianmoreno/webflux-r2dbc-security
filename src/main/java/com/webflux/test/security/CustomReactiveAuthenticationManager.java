package com.webflux.test.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import reactor.core.publisher.Mono;

@Component
public class CustomReactiveAuthenticationManager implements ReactiveAuthenticationManager {

    @Autowired
    private CustomReactiveAuthenticationProvider customReactiveAuthenticationProvider;

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        return customReactiveAuthenticationProvider.authenticate(authentication)
        .map((result) -> {
            ReactiveSecurityContextHolder.withAuthentication(result);    
            return result;
        });
    }
    
}
