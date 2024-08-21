package com.webflux.test.interfaces;

import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.webflux.test.annotations.AdminPermission;

import reactor.core.publisher.Mono;

public interface IUserController {
    Mono<ServerResponse> save(ServerRequest request);

    Mono<ServerResponse> findById(ServerRequest request);

    @AdminPermission
    Mono<ServerResponse> findAll(ServerRequest request);
}
