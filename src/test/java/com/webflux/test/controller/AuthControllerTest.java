package com.webflux.test.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.webflux.test.dto.LoginDTO;
import com.webflux.test.entity.Users;
import com.webflux.test.roles.Roles;
import com.webflux.test.services.UserRepositoryService;
import reactor.core.publisher.Mono;

@SpringBootTest
@AutoConfigureWebTestClient
public class AuthControllerTest {
    
    private WebTestClient webTestClient;

    @Autowired
    private RouterFunction<ServerResponse> authRoutes;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @MockBean
    private UserRepositoryService userRepositoryService;

    private LoginDTO loginDTO;
    private Users users;

    @BeforeEach
    void setup() {
        webTestClient = WebTestClient.bindToRouterFunction(authRoutes).build();

        loginDTO = new LoginDTO();
        loginDTO.setUsername("user");
        loginDTO.setPassword("user");

        users = new Users();
        users.setUsername("user");
        users.setPassword(passwordEncoder.encode("user"));
        users.setAuthority(Roles.ROLE_ADMIN, Roles.ROLE_SAVE);
    }

    @Test
    void testLogin() {
        // GIVEN
        given(userRepositoryService.findByUsername(users.getUsername())).willReturn(Mono.just(users));

        // WHEN && THEN
        webTestClient
        .post()
        .uri("/auth/login")
        .bodyValue(loginDTO)
        .exchange()
        .expectBody()
        .consumeWith((result) -> {
            System.out.println(result);
            assertNotNull(result);
            assertEquals(HttpStatus.OK, result.getStatus());
        });
    }

    @Test
    void testUserNotFound() {
        // GIVEN
        given(userRepositoryService.findByUsername(loginDTO.getUsername())).willReturn(Mono.empty());

        // WHEN & THEN
        webTestClient
        .post()
        .uri("/auth/login")
        .bodyValue(loginDTO)
        .exchange()
        .expectStatus().isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void testAuthenticationFailed() {
        // GIVEN
        loginDTO.setPassword(passwordEncoder.encode("users"));
        given(userRepositoryService.findByUsername(loginDTO.getUsername())).willReturn(Mono.just(users));

        // WHEN && THEN
        webTestClient
        .post()
        .uri("/auth/login")
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(loginDTO)
        .exchange()
        .expectBody()
        .consumeWith((response) -> {
            assertNotNull(response);
            assertEquals(HttpStatus.UNAUTHORIZED, response.getStatus());
        });
    }
}
