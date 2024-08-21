package com.webflux.test.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.webflux.test.interfaces.IReactiveAuthenticationProvider;

import reactor.core.publisher.Mono;

@Service
public class CustomReactiveAuthenticationProvider implements IReactiveAuthenticationProvider {

    @Autowired
    private CustomReactiveUserDetailsService customReactiveUserDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();

        return customReactiveUserDetailsService.findByUsername(username)
        .flatMap((u) -> {
            if (!passwordEncoder.matches(password, u.getPassword())) {
                return Mono.error(new BadCredentialsException("Bad Credentials!")); 
            };

            Authentication authenticate = new UsernamePasswordAuthenticationToken(
                u.getUsername(), 
                u.getPassword(), 
                u.getAuthorities()
            );

            return Mono.just(authenticate);
        });
    }
    
}
