package com.webflux.test.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.webflux.test.dto.AuthResultDTO;
import com.webflux.test.dto.LoginDTO;
import com.webflux.test.entity.Users;
import com.webflux.test.interfaces.IAuthController;
import com.webflux.test.security.CustomReactiveAuthenticationManager;
import com.webflux.test.services.JwkService;

import reactor.core.publisher.Mono;

@Component
public class AuthController implements IAuthController {

    @Autowired
    private CustomReactiveAuthenticationManager customReactiveAuthenticationManager;    

    @Autowired
    private JwkService jwkService;

    @Override
    public Mono<ServerResponse> login(ServerRequest request) {
        return request.bodyToMono(LoginDTO.class)
        .flatMap((auth) -> {
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                auth.getUsername(),
                auth.getPassword()
            );

            return customReactiveAuthenticationManager.authenticate(usernamePasswordAuthenticationToken);
        })
        .flatMap((result) -> {
            String token = jwkService.encode(result);

            AuthResultDTO authResultDTO = new AuthResultDTO();
            authResultDTO.setToken(token);
            authResultDTO.setUsername(result.getName());
            authResultDTO.setAuthority(result.getAuthorities());

            return ServerResponse.ok().bodyValue(authResultDTO);
        })
        .switchIfEmpty(ServerResponse.notFound().build())
        .onErrorResume((exception) -> ServerResponse.status(HttpStatus.UNAUTHORIZED).bodyValue(exception.getMessage()));
    }
}
