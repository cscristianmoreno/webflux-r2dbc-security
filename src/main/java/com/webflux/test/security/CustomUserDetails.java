package com.webflux.test.security;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsChecker;

import java.util.*;

import com.webflux.test.entity.Users;

public class CustomUserDetails implements UserDetails {
    private final Users users;

    public CustomUserDetails(final Users users) {
        this.users = users;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();

        for (String roles: users.getAuthority()) {
            authorities.add(new SimpleGrantedAuthority(roles));
        }

        return authorities;
    }

    @Override
    public String getPassword() {
        return users.getPassword();
    }

    @Override
    public String getUsername() {
        return users.getUsername();
    }
}
