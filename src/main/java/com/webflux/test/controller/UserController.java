package com.webflux.test.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.webflux.test.entity.Users;
import com.webflux.test.interfaces.IUserController;
import com.webflux.test.services.UserRepositoryService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class UserController implements IUserController {

    @Autowired
    private UserRepositoryService userRepositoryService;

    @Override
    public Mono<ServerResponse> save(ServerRequest request) {
        return request
        .bodyToMono(Users.class)
        .flatMap((result) -> {
            return userRepositoryService.save(result);
        })
        .flatMap((result) -> {
            return ServerResponse.ok().bodyValue(result);
        });
    }

    @Override
    public Mono<ServerResponse> findById(ServerRequest request) {
        int id = Integer.parseInt(request.pathVariable("id"));

        return userRepositoryService.findById(id)
        .flatMap((result) -> ServerResponse.ok().bodyValue(result))
        .switchIfEmpty(ServerResponse.notFound().build());
    }

    @Override
    public Mono<ServerResponse> findAll(ServerRequest request) {
        return userRepositoryService.findAll()
        .collectList()
        .flatMap((result) -> ServerResponse.ok().bodyValue(result));
    }
}
