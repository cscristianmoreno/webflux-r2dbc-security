package com.webflux.test.services;

import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

@Service
public class JwkService {

    @Autowired
    private JwtEncoder jwtEncoder;

    public String encode(Authentication authentication) {
        String username = authentication.getName();
        Collection<? extends GrantedAuthority> authority = authentication.getAuthorities();

        List<String> authorities = authority.stream().map(GrantedAuthority::getAuthority)
        .toList();

        Instant instant = Instant.now();
        long expire = 3600L;

        JwtClaimsSet claims = JwtClaimsSet
        .builder()
            .issuer("secret-issuer")
            .issuedAt(instant)
            .expiresAt(instant.plusSeconds(expire))
            .subject(username)
            .claim("scope", authorities)
        .build();

        return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }
    
}
