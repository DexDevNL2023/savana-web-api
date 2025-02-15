package com.savana.proxy.filter;

import com.savana.proxy.utils.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.function.Predicate;

@Slf4j
@Component
public class JwtAuthenticationFilter implements GatewayFilter {
    private final JwtUtil jwtUtil;

    public JwtAuthenticationFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();

        Predicate<ServerHttpRequest> isApiSecured = getIsApiSecured();

        if (isApiSecured.test(request)) {
            if (authMissing(request)) return onError(exchange);

            String token = request.getHeaders().getOrEmpty("Authorization").get(0);

            if (token != null && token.startsWith("Bearer ")) token = token.substring(7);

            if (jwtUtil.isInvalid(token)) {
                log.error("Token expired");
                return this.onError(exchange);
            }

            try {
                jwtUtil.validateToken(token);
            } catch (Exception e) {
                log.error("Invalid Token: {}", e.getMessage());
                return onError(exchange);
            }
        }
        return chain.filter(exchange);
    }

    private static Predicate<ServerHttpRequest> getIsApiSecured() {
        final List<String> apiEndpoints = List.of("/eureka", "/api/profile/auth/**", "/openapi/**",
                "/webjars/**", "/swagger-resources/**", "/v3/api-docs/**", "/v3/api-docs.yaml", "/swagger-ui/**",
                "/swagger-ui.html", "/static/**", "/error/**", "/configuration/ui", "/configuration/security",
                "/actuator/**", "/api/files/**", "/data/uploads/**", "/favicon.ico");

        return r -> apiEndpoints.stream().noneMatch(uri -> r.getURI().getPath().matches(uri.replace("**", ".*")));
    }

    private Mono<Void> onError(ServerWebExchange exchange) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        return response.setComplete();
    }

    private boolean authMissing(ServerHttpRequest request) {
        return !request.getHeaders().containsKey("Authorization");
    }
}