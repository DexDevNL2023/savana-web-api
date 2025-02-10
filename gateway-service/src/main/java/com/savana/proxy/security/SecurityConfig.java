package com.savana.proxy.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import reactor.core.publisher.Mono;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(SecurityConfig.class);

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable) // Désactiver CSRF pour les API REST
                .cors(cors -> cors.configurationSource(corsConfigurationSource())) // Activer CORS
                .authorizeExchange(exchange -> exchange
                        .pathMatchers(HttpMethod.OPTIONS).permitAll() // Autoriser préflight CORS
                        .pathMatchers(AUTH_WHITELIST).permitAll() // Routes publiques
                        .anyExchange().authenticated() // Authentification requise pour le reste
                )
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .authenticationEntryPoint((exchange, ex) -> {
                            LOGGER.warn("Requête non autorisée : {}", exchange.getRequest().getURI());
                            return Mono.error(ex);
                        })
                        .accessDeniedHandler((exchange, ex) -> {
                            LOGGER.warn("Accès refusé à : {}", exchange.getRequest().getURI());
                            return Mono.error(ex);
                        })
                )
                .build();
    }

    @Bean
    public UrlBasedCorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("*")); // Autoriser toutes les origines (*)
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("Authorization", "Content-Type"));
        config.setExposedHeaders(List.of("Authorization"));
        config.setAllowCredentials(false);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    private static final String[] AUTH_WHITELIST = {
            "/webjars/**",
            "/swagger-resources/**",
            "/v3/api-docs/**",
            "/v3/api-docs.yaml",
            "/swagger-ui/**",
            "/swagger-ui.html",
            "/webjars/**",
            "/static/**",
            "/error/**",
            "/configuration/ui",
            "/configuration/security",

            // Permet l'accès aux endpoints actuator
            "/actuator/**",

            // Routes publiques
            "/api/authentifications/**",
            "/api/files/**", // Autoriser l'accès aux fichiers dans ce dossier

            // Téléchargement de rapports
            "/data/uploads/**",
            "/favicon.ico"
    };
}
