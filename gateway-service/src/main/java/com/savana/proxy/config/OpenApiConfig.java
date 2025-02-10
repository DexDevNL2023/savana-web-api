package com.savana.proxy.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    // Bean principal pour configurer OpenAPI
    @Bean
    public OpenAPI openAPI() {
        final String securitySchemeName = "Authorization";
        return new OpenAPI()
                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
                .components(new Components()
                        .addSecuritySchemes(securitySchemeName, new SecurityScheme()
                                .name(securitySchemeName)
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                        )
                );
    }

    // Exposer les API spécifiques de chaque microservice via la Gateway
    @Bean
    public GroupedOpenApi accountingApi() {
        return GroupedOpenApi.builder()
                .group("account-service-users")
                .pathsToMatch("/api/profile/users/**")
                .build();
    }

    @Bean
    public GroupedOpenApi authApi() {
        return GroupedOpenApi.builder()
                .group("accounting-service-journal")
                .pathsToMatch("/api/accounting/journal/**")
                .build();
    }

    @Bean
    public GroupedOpenApi insuranceApi() {
        return GroupedOpenApi.builder()
                .group("accounting-service-comptes")
                .pathsToMatch("/api/accounting/comptes/**")
                .build();
    }

    @Bean
    public GroupedOpenApi claimApi() {
        return GroupedOpenApi.builder()
                .group("account-service-auth")
                .pathsToMatch("/api/profile/auth/**")
                .build();
    }
}
