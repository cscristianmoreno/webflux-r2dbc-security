package com.webflux.test.security;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.BadJwtException;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.WebFilter;

import com.webflux.test.roles.Roles;
import com.webflux.test.services.JwkService;

@SpringBootTest
@AutoConfigureWebTestClient
public class CustomWebFilterTest {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private RouterFunction<ServerResponse> userRoutes;

    @Autowired
    private CustomWebFilter webFilter;

    @Autowired
    private JwkService jwkService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setup() {
        webTestClient = WebTestClient.bindToRouterFunction(userRoutes).webFilter(webFilter).build();
    }

    @Test
    void testFilterPassedAuthentication() {

        List<GrantedAuthority> authority = AuthorityUtils.createAuthorityList(Roles.ROLE_READ.name(), Roles.ROLE_SAVE.name());

        Authentication authentication = new UsernamePasswordAuthenticationToken(
            "user",
            passwordEncoder.encode("user"),
            authority
        );

        String token = jwkService.encode(authentication);

        webTestClient
        .get()
        .uri("/users/{id}", 1)
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
        .exchange()
        .expectBody()
        .consumeWith((response) -> {
            System.out.println(response.getRequestHeaders().get(HttpHeaders.AUTHORIZATION));
            assertNotNull(response);
            assertEquals(HttpStatus.UNAUTHORIZED, response.getStatus());
        });
    }

    @Test
    void testFilterFailureAuthentication() {
        webTestClient
        .get()
        .uri("/users/{id}", 1)
        .exchange()
        .expectBody()
        .consumeWith((response) -> {
            assertNotNull(response);
            assertEquals(HttpStatus.UNAUTHORIZED, response.getStatus());
        });
    }
}
