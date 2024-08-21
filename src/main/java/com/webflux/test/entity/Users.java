package com.webflux.test.entity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.http.HttpStatus;

import com.webflux.test.roles.Roles;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Table("users")
@Getter
@Setter
@ToString
public class Users {
    @Id
    private int id;

    private String username;
    private String password;
    
    private List<String> authority = new ArrayList<String>();

    public void setAuthority(Roles... roles) {
        for (Roles role: roles) {
            authority.add(role.name());
        }
    }
}
