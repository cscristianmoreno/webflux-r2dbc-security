package com.webflux.test.security;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.BDDMockito.given;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.webflux.test.entity.Users;
import com.webflux.test.roles.Roles;

import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
public class CustomReactiveAuthenticationManagerTest {

    @Mock
    private CustomReactiveAuthenticationProvider customReactiveAuthenticationProvider;

    @InjectMocks
    private CustomReactiveAuthenticationManager customReactiveAuthenticationManager;
    
    private Users users;
    private UserDetails userDetails;

    @BeforeEach
    void setup() {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        users = new Users();
        users.setUsername("user");
        users.setPassword(passwordEncoder.encode("user"));
        users.setAuthority(Roles.ROLE_ADMIN, Roles.ROLE_SAVE);

        userDetails = new CustomUserDetails(users);
    }

    @Test
    void testAuthenticate() {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
            userDetails.getUsername(),
            userDetails.getPassword(),
            userDetails.getAuthorities()
        );

        // GIVEN
        given(customReactiveAuthenticationProvider.authenticate(usernamePasswordAuthenticationToken)).willReturn(Mono.just(usernamePasswordAuthenticationToken));

        // WHEN
        Mono<Authentication> authentication = customReactiveAuthenticationManager.authenticate(usernamePasswordAuthenticationToken);

        // THEN
        StepVerifier.create(authentication)
        .assertNext((result) -> {
            assertNotNull(result);
            assertEquals(UsernamePasswordAuthenticationToken.class, result.getClass());
            assertTrue(result.getAuthorities().size() > 0);
        })
        .expectComplete()
        .verify();
    }
}
