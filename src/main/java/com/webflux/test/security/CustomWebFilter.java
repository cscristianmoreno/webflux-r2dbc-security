package com.webflux.test.security;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.codec.DataBufferEncoder;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.BadJwtException;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;

import reactor.core.publisher.Mono;

@Component
public class CustomWebFilter implements WebFilter {

    @Autowired
    private JwtDecoder jwtDecoder;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        String path = exchange.getRequest().getPath().value();
        
        if (path.contains("auth/login") || path.contains("users/save")) {
            return chain.filter(exchange);
        }
        
        HttpHeaders headers = exchange.getRequest().getHeaders();
        
        List<String> listAuthorization = headers.get("Authorization");
        
        if (listAuthorization == null || listAuthorization.isEmpty()) {
            return unauthenticate(exchange);
        }

        String authorization = listAuthorization.get(0);

        if (!authorization.startsWith("Bearer ")) {
            return unauthenticate(exchange);
        }

        String[] splitToken = authorization.trim().split(" ");
        String token = splitToken[1];
        
        if (token.length() == 0) {
            return unauthenticate(exchange);
        }
        
        try {
            Jwt claims = jwtDecoder.decode(token);
            Authentication authentication = getAuthentication(claims);
            
            return ReactiveSecurityContextHolder
            .getContext()
            .doOnNext((sc) -> {
                sc.setAuthentication(authentication);
            })
            .then(chain.filter(exchange));
        }
        catch (BadJwtException e) {
            return Mono.error(new BadJwtException("Token is invalid"));
        }
    }

    private Mono<Void> unauthenticate(final ServerWebExchange exchange) {
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        return exchange.getResponse().setComplete();
    }

    private Authentication getAuthentication(final Jwt claims) {
        String username = claims.getSubject();
        List<String> authorities = claims.getClaim("scope");
        List<GrantedAuthority> authority = AuthorityUtils.createAuthorityList(authorities.stream().toList());
        
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(username, 
        null, authority);

        return usernamePasswordAuthenticationToken;

    }
}
