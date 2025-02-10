package com.savana.proxy.security;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.function.Predicate;

@Component
public class RouterValidator {
    // Fonction qui vérifie si une route nécessite de la sécurité
    public final Predicate<ServerHttpRequest> isSecured = request -> {
        String path = request.getURI().getPath();
        return path.startsWith("/api/profile/users/**") || path.startsWith("/api/accounting/**");
    };
}