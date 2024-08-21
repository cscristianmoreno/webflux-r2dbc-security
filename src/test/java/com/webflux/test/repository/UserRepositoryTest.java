package com.webflux.test.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.time.Duration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;

import com.webflux.test.entity.Users;
import com.webflux.test.roles.Roles;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@DataR2dbcTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    private Users users;

    @BeforeEach
    void setup() { 
        users = new Users();
        users.setUsername("user");
        users.setPassword("user");
        users.setAuthority(Roles.ROLE_ADMIN, Roles.ROLE_DELETE);
    }

    @Test
    void save() {
        users.setUsername("pablo");
        Mono<Users> user = userRepository.save(users);
        
        StepVerifier
        .create(user)
        .expectNext(users)
        .expectComplete()
        .verify();
    }

    @Test
    void findById() throws InterruptedException {
        
        StepVerifier.create(userRepository.save(users))
        .expectNext(users)
        .expectComplete()
        .verify();

        StepVerifier.create(userRepository.findById(1))
        .assertNext((result) -> {
            System.out.println(result);
            assertNotNull(result);
            assertEquals(users.getUsername(), result.getUsername());
        })
        .expectComplete()
        .verify();
    }

    @Test
    void testFindByUsername() {
        StepVerifier.create(userRepository.save(users))
        .expectNext(users)
        .expectComplete()
        .verify();

        StepVerifier.create(userRepository.findByUsername(users.getUsername()))
        .assertNext((result) -> {
            assertNotNull(users);
            assertEquals(users.getUsername(), result.getUsername());
            assertTrue(result.getAuthority().size() > 0);
        })
        .expectComplete()
        .verify();
    }
}
