package com.webflux.test.connection;

import java.util.Properties;

import org.springframework.boot.r2dbc.ConnectionFactoryBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.r2dbc.connection.init.ConnectionFactoryInitializer;
import org.springframework.r2dbc.connection.init.ResourceDatabasePopulator;

import com.webflux.test.TestApplication;

import io.r2dbc.postgresql.PostgresqlConnectionConfiguration;
import io.r2dbc.postgresql.PostgresqlConnectionConfiguration.Builder;
import io.r2dbc.postgresql.PostgresqlConnectionFactory;
import io.r2dbc.spi.ConnectionFactory;

@Configuration
public class ConnectionDatabase {
    
    @Bean
    ConnectionFactoryInitializer initializer(ConnectionFactory connectionFactory) {
        ConnectionFactoryInitializer connectionFactoryInitializer = new ConnectionFactoryInitializer();
        connectionFactoryInitializer.setConnectionFactory(connectionFactory);
        connectionFactoryInitializer.setDatabasePopulator(new ResourceDatabasePopulator(
            new ClassPathResource("schema.sql")
        ));

        return connectionFactoryInitializer;
    }
}
