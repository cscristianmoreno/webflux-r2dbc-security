package com.webflux.test.routes;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RequestPredicate;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.webflux.test.controller.AuthController;
import com.webflux.test.controller.UserController;

@Configuration
public class RoutesConfig {
    @Bean 
    RouterFunction<ServerResponse> userRoutes(UserController userController) {
        RequestPredicate requestSave = RequestPredicates.POST("/users/save").and(RequestPredicates.contentType(MediaType.APPLICATION_JSON));
        RequestPredicate requestFindById = RequestPredicates.GET("/users/{id}/");
        RequestPredicate requestFindAll = RequestPredicates.GET("/users/all");       
        
        return RouterFunctions
        .route(requestSave, userController::save)
        .andRoute(requestFindById, userController::findById)
        .andRoute(requestFindAll, userController::findAll);
    }

    @Bean 
    RouterFunction<ServerResponse> authRoutes(AuthController authController) {
        return RouterFunctions.route(RequestPredicates.POST("/auth/login"), authController::login);
    }
}
