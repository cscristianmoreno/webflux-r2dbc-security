package com.webflux.test.interfaces;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IRepository<T> {
    
    Mono<T> save(T entity);

    Mono<T> findById(int id);

    Flux<T> findAll();
}
