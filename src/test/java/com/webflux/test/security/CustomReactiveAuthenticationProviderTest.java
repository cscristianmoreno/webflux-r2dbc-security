package com.webflux.test.security;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.BDDMockito.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.webflux.test.entity.Users;
import com.webflux.test.roles.Roles;

import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
public class CustomReactiveAuthenticationProviderTest {

    @Mock
    private CustomReactiveUserDetailsService customReactiveUserDetailsService;

    @Mock
    private PasswordEncoder mockPasswordEncoder;

    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private CustomReactiveAuthenticationProvider customReactiveAuthenticationProvider;

    private Users users;
    private CustomUserDetails customUserDetails;

    @BeforeEach
    void setup() {
        passwordEncoder = new BCryptPasswordEncoder();

        users = new Users();
        users.setUsername("user");
        users.setPassword(passwordEncoder.encode("user"));
        users.setAuthority(Roles.ROLE_ADMIN, Roles.ROLE_SAVE);

        customUserDetails = new CustomUserDetails(users);
    }

    @Test
    void testAuthenticate() {
        // GIVEN
        given(customReactiveUserDetailsService.findByUsername(users.getUsername())).willReturn(Mono.just(customUserDetails));
        given(mockPasswordEncoder.encode("user")).willReturn(users.getPassword());
        
        // WHEN
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
            users.getUsername(),
            "user"
        );

        Mono<Authentication> authentication = customReactiveAuthenticationProvider.authenticate(usernamePasswordAuthenticationToken);

        // THEN 
        StepVerifier.create(authentication)
        .assertNext((result) -> {
            System.out.println(result.getCredentials().toString());
            assertNotNull(result);
            assertEquals(UsernamePasswordAuthenticationToken.class, result.getClass());
        })
        .expectComplete()
        .verify();
    }
}
