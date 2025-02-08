package com.savana.accounting.rest.config;

import com.savana.accounting.generic.utils.AppConstants;
import feign.Logger;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryConfig;
import io.github.resilience4j.retry.RetryRegistry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.FeignClientsConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.time.Duration;

@Configuration
@EnableDiscoveryClient  // Activation de Eureka Discovery Client
@Import(FeignClientsConfiguration.class)
public class FeignConfiguration {

    private final MyJWTConfig myJWTConfig;

    @Value("${spring.application.name}")
    private String applicationName;

    public FeignConfiguration(MyJWTConfig myJWTConfig) {
        this.myJWTConfig = myJWTConfig;
    }

    @Bean
    public RequestInterceptor requestInterceptor() {
        return new CustomFeignClientInterceptor(myJWTConfig.getSecret());
    }

    @Bean
    public Retry retry() {
        RetryConfig retryConfig = RetryConfig.custom()
                .maxAttempts(3) // Nombre de tentatives avant échec
                .waitDuration(Duration.ofMillis(500)) // Temps d'attente entre les tentatives
                .build();

        return RetryRegistry.of(retryConfig).retry(applicationName);
    }

    @Bean
    Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }

    private static class CustomFeignClientInterceptor implements RequestInterceptor {

        private final String jwtToken;

        public CustomFeignClientInterceptor(String jwtToken) {
            this.jwtToken = jwtToken;
        }

        @Override
        public void apply(RequestTemplate template) {
            // Ajoute le JWT dans l'en-tête de chaque requête Feign
            template.header(AppConstants.AUTHORIZATION_HEADER, String.format("%s %s", AppConstants.BEARER, jwtToken));
        }
    }
}
