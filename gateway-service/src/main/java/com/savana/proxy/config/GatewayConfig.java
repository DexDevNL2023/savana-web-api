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
                .route("account-service-users", r -> r.path("/api/profile/users/**")
                        .filters(f -> f.filter(this.filter))
                        .uri("lb://AUTH-SERVICE"))
                .route("accounting-service-journal", r -> r.path("/api/accounting/journal/**")
                        .filters(f -> f.filter(this.filter))
                        .uri("lb://ACCOUNTING-SERVICE"))
                .route("accounting-service-comptes", r -> r.path("/api/accounting/comptes/**")
                        .filters(f -> f.filter(this.filter))
                        .uri("lb://ACCOUNTING-SERVICE"))
                .route("account-service-auth", r -> r.path("/api/profile/auth/**")
                        .uri("lb://AUTH-SERVICE"))
                // Routes publiques pour Swagger
                .route("swagger-auth", r -> r.path("/api/profile/swagger-ui/**")
                        .uri("lb://AUTH-SERVICE"))
                .route("swagger-accounting", r -> r.path("/api/accounting/swagger-ui/**")
                        .uri("lb://ACCOUNTING-SERVICE"))
                .route("swagger-ui", r -> r.path("/swagger-ui/**")
                        .uri("lb://GATEWAY-SERVICE"))
                .build();
    }
}
