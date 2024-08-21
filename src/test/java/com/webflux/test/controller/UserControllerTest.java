package com.webflux.test.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.webflux.test.entity.Users;
import com.webflux.test.repository.UserRepository;
import com.webflux.test.services.UserRepositoryService;

import reactor.test.StepVerifier;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserControllerTest {
    
    @Autowired
    private RouterFunction<ServerResponse> userRoutes;

    private WebTestClient webTestClient;
    private Users users;

    @Autowired
    private UserRepositoryService userRepositoryService;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setupBefore() {
        webTestClient = WebTestClient.bindToRouterFunction(userRoutes).build();
        users = new Users();
    }

    @Test
    @Order(1)
    void testSave() {
        users.setUsername("user");
        users.setPassword("user");

        webTestClient
        .post()
        .uri("/users/save")
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(users)
        .exchange()
        .expectBody()
        .consumeWith((response) -> {
            assertNotNull(response);
            assertEquals(HttpStatus.OK, response.getStatus());
        });
    }

    @Test
    @Order(2)
    void testFindById() {
        users.setUsername("user");
        users.setPassword("user");

        StepVerifier
        .create(userRepositoryService.save(users))
        .assertNext((result) -> {
            assertNotNull(result);
        })
        .expectComplete()
        .verify();
        
        webTestClient
        .get()
        .uri("/users/{id}/", 1)
        .exchange()
        .expectStatus().isEqualTo(HttpStatus.OK)
        .expectBody(Users.class)
        .consumeWith((result) -> {
            assertNotNull(result);
            assertEquals(Users.class, result.getResponseBody().getClass());
            assertEquals(HttpStatus.OK, result.getStatus());
        });
    }

    @Test
    @Order(3)
    void testFindAll() {

        webTestClient
        .get()
        .uri("/users/all")
        .exchange()
        .expectBody()
        .consumeWith((response) -> {
            System.out.println(response);
            assertNotNull(response);
            assertEquals(HttpStatus.OK, response.getStatus());
        });
    }
}
