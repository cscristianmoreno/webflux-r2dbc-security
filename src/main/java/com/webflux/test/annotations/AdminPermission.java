package com.webflux.test.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import org.springframework.security.access.prepost.PreAuthorize;

@Retention(RetentionPolicy.RUNTIME)
@PreAuthorize("hasAuthority('ROLE_ADMIN')")
public @interface AdminPermission {
}
