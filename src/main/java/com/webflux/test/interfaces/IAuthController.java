package com.webflux.test.interfaces;

import org.springframework.security.core.Authentication;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import reactor.core.publisher.Mono;

public interface IAuthController {
    Mono<ServerResponse> login(ServerRequest request);
}
