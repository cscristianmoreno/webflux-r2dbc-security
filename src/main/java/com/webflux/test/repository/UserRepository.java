package com.webflux.test.repository;

import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.webflux.test.entity.Users;

import reactor.core.publisher.Mono;

@EnableR2dbcRepositories
public interface UserRepository extends ReactiveCrudRepository<Users, Integer> {
    Mono<Users> findById(int id);

    Mono<Users> findByUsername(String username);
}
