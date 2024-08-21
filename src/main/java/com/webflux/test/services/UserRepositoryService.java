package com.webflux.test.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.webflux.test.entity.Users;
import com.webflux.test.interfaces.ICrudRepository;
import com.webflux.test.repository.UserRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class UserRepositoryService implements ICrudRepository<Users> {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Mono<Users> save(Users entity) {
        String password = entity.getPassword();
        entity.setPassword(passwordEncoder.encode(password));
        return userRepository.save(entity);
    }

    @Override
    public Mono<Users> findById(int id) {
        return userRepository.findById(id);
    }

    @Override
    public Flux<Users> findAll() {
        return userRepository.findAll();
    }

    @Override
    public Mono<Users> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
    
}
