package com.webflux.test.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder.BCryptVersion;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class CustomPasswordEncoder {
    
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
