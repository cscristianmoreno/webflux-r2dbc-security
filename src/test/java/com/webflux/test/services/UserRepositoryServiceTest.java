package com.webflux.test.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.BDDMockito.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.webflux.test.entity.Users;
import com.webflux.test.repository.UserRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
public class UserRepositoryServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserRepositoryService userRepositoryService;

    private Users users;

    @BeforeEach
    void setup() {
        users = new Users();
        users.setUsername("user");
        users.setPassword("user");
    }

    @Test
    void testSave() {
        // GIVEN
        given(userRepository.save(users)).willReturn(Mono.just(users));

        // WHEN
        Mono<Users> user = userRepositoryService.save(users);

        // THEN
        StepVerifier.create(user)
        .assertNext((result) -> {
            assertNotNull(result);
            assertEquals(users.getUsername(), result.getUsername());
        })
        .expectComplete()
        .verify();
    }
    
    @Test
    void testFindById() {

        // GIVEN
        given(userRepository.findById(1)).willReturn(Mono.just(users));

        // WHEN
        Mono<Users> user = userRepositoryService.findById(1);

        // THEN
        StepVerifier
        .create(user)
        .expectNext(users)
        .expectComplete()
        .verify();
    }
    
    @Test
    void testFindAll() {
        Flux<Users> list = Flux.just(users, users);

        // GIVEN
        given(userRepository.findAll()).willReturn(list);

        // WHEN
        Flux<Users> saveAll = userRepository.findAll();

        // THEN
        StepVerifier.create(saveAll)
        .expectNextSequence(list.toStream().toList())
        .expectComplete()
        .verify();
    }

    @Test
    void testFindByUsername() {
        // GIVEN
        given(userRepository.findByUsername(users.getUsername())).willReturn(Mono.just(users));

        // WHEN
        Mono<Users> user = userRepositoryService.findByUsername(users.getUsername());

        // THEN
        StepVerifier.create(user)
        .expectNext(users)
        .expectComplete()
        .verify();
    }

}
