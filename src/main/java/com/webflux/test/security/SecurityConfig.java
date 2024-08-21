package com.webflux.test.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity(useAuthorizationManager = true)
public class SecurityConfig {

    @Autowired
    private CustomReactiveAuthenticationManager customReactiveAuthenticationManager;

    @Autowired
    private CustomWebFilter customWebFilter;

    @Bean
    SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity serverHttpSecurity) throws Exception {
        return (
            serverHttpSecurity
                .authorizeExchange((exchange) -> {
                    exchange.pathMatchers("/users/save", "/auth/login").permitAll();
                    exchange.pathMatchers("/auth/login").permitAll();
                    exchange.anyExchange().authenticated();
                })
                .authenticationManager(customReactiveAuthenticationManager)
                .addFilterBefore(customWebFilter, SecurityWebFiltersOrder.AUTHENTICATION)
                .csrf((csrf) -> {
                    csrf.disable();
                })
                .httpBasic(Customizer.withDefaults())
            .build()
        );
    }
}
