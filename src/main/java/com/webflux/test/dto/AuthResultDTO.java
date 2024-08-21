package com.webflux.test.dto;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class AuthResultDTO {
    private int id;
    private String username;
    private Collection<? extends GrantedAuthority> authority;
    private String token;    
}
