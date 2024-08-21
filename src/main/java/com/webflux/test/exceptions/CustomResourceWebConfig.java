package com.webflux.test.exceptions;

import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CustomResourceWebConfig {
    
    @Bean
    WebProperties.Resources resources() {
        return new WebProperties.Resources();
    }
}
