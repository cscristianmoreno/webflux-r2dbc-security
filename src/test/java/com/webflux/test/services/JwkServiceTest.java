package com.webflux.test.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;

import com.webflux.test.roles.Roles;

@SpringBootTest
public class JwkServiceTest {
    
    @Autowired
    private JwkService jwkService;

    @Autowired 
    private PasswordEncoder passwordEncoder;

    @Test
    void testEncode() {
        List<GrantedAuthority> authority = AuthorityUtils.createAuthorityList(Roles.ROLE_ADMIN.name(), Roles.ROLE_DELETE.name());
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
            "user",
            passwordEncoder.encode("user"),
            authority
        );

        String token = jwkService.encode(usernamePasswordAuthenticationToken);
        
        assertNotNull(token);
        System.out.println(token);
    }
}
