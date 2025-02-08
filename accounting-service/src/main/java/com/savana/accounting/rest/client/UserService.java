package com.savana.accounting.rest.client;

import com.savana.accounting.rest.config.FeignConfiguration;
import com.savana.accounting.rest.model.UserResponse;
import jakarta.validation.constraints.NotNull;
import com.savana.accounting.rest.config.GenericFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

@FeignClient(name = "auth-service", configuration = FeignConfiguration.class, fallbackFactory = GenericFallbackFactory.class)
public interface UserService {
    @GetMapping("/api/autorisations/current/user/name")
    Optional<String> getCurrentUserLogin();

    @GetMapping("/api/autorisations/current/user/jwt")
    Optional<String> getCurrentUserJWT();

    @GetMapping("/api/accounts/find/{id}?projection=UserProjection")
    UserResponse getUserById(@NotNull @PathVariable("id") Long id);
}
