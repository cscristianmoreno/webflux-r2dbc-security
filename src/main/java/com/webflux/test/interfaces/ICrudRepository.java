package com.webflux.test.interfaces;

import reactor.core.publisher.Mono;

public interface ICrudRepository<T> extends IRepository<T> {
    Mono<T> findByUsername(String username);
}
