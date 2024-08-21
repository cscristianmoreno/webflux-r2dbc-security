package com.webflux.test.utils;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

public abstract class RoutesUtil {
    
    static RouterFunction<ServerResponse> routerFunctions = RouterFunctions.route().build();

    
}
