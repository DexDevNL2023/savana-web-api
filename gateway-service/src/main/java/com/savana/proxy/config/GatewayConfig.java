package com.savana.proxy.config;

import com.savana.proxy.security.JwtFilter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    private final JwtFilter filter;

    public GatewayConfig(JwtFilter filter) {
        this.filter = filter;
    }

    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("account-service", r -> r.path("/api/accounts/**")
                        .filters(f -> f.filter(this.filter)) // Application du filtre personnalisé
                        .uri("lb://AUTH-SERVICE"))
                .route("accounting-service-journal", r -> r.path("/api/accounting/journal/**")
                        .filters(f -> f.filter(this.filter)) // Application du filtre personnalisé
                        .uri("lb://ACCOUNTING-SERVICE"))
                .route("accounting-service-comptes", r -> r.path("/api/comptes/**")
                        .filters(f -> f.filter(this.filter)) // Application du filtre personnalisé
                        .uri("lb://ACCOUNTING-SERVICE"))
                .route("auth-service", r -> r.path("/api/authentifications/**")
                        .uri("lb://AUTH-SERVICE")) // Cette route est publique, pas de filtre
                // Routes publiques pour Swagger
                .route("swagger-auth", r -> r.path("/auth-service/v3/api-docs")
                        .uri("lb://AUTH-SERVICE"))
                .route("swagger-accounting", r -> r.path("/accounting-service/v3/api-docs")
                        .uri("lb://ACCOUNTING-SERVICE"))
                .route("swagger-ui", r -> r.path("/swagger-ui/**")
                        .uri("lb://GATEWAY-SERVICE"))
                .build();
    }
}
