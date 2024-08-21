package com.webflux.test.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import com.webflux.test.services.UserRepositoryService;

import reactor.core.publisher.Mono;

@Service
public class CustomReactiveUserDetailsService implements ReactiveUserDetailsService {

    @Autowired
    private UserRepositoryService userRepositoryService;

    @Override
    public Mono<UserDetails> findByUsername(String username) {
        return userRepositoryService.findByUsername(username)
        .map((result) -> {
            return new CustomUserDetails(result);
        });
    }
    
}
