package com.savana.proxy.config;

import com.savana.proxy.filter.JwtAuthenticationFilter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {
    private final JwtAuthenticationFilter filter;

    public GatewayConfig(JwtAuthenticationFilter filter) {
        this.filter = filter;
    }

    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("accounting-service", r -> r.path("/api/accounting/**")
                        .filters(f -> f.filter(filter))
                        .uri("lb://ACCOUNTING-SERVICE"))

                .route("user-service", r -> r.path("/api/profile/users/**")
                        .filters(f -> f.filter(filter))
                        .uri("lb://AUTH-SERVICE"))

                .route("auth-service", r -> r.path("/api/profile/auth/**")
                        .uri("lb://AUTH-SERVICE"))

                .route("openapi", r -> r.path("/openapi/**")
                        .filters(f -> f.rewritePath("/openapi/(?<segment>.*)", "/v3/api-docs"))
                        .uri("lb://GATEWAY-SERVICE"))

                .route("auth-api-docs", r -> r.path("/v3/api-docs/profile")
                        .filters(f -> f.rewritePath("/v3/api-docs/profile", "/v3/api-docs"))
                        .uri("lb://AUTH-SERVICE"))

                .route("auth-swagger-ui", r -> r.path("/swagger-ui/profile")
                        .filters(f -> f.rewritePath("/swagger-ui/profile", "/swagger-ui/index.html#/"))
                        .uri("lb://AUTH-SERVICE"))

                .route("accounting-api-docs", r -> r.path("/v3/api-docs/accounting")
                        .filters(f -> f.rewritePath("/v3/api-docs/accounting", "/v3/api-docs"))
                        .uri("lb://ACCOUNTING-SERVICE"))

                .route("accounting-swagger-ui", r -> r.path("/swagger-ui/accounting")
                        .filters(f -> f.rewritePath("/swagger-ui/accounting", "/swagger-ui/index.html#/"))
                        .uri("lb://ACCOUNTING-SERVICE"))

                .build();
    }
}