package com.savana.auth.projection;

import com.savana.auth.authentification.entities.Account;
import org.springframework.data.rest.core.config.Projection;

@Projection(name = "UserProjection", types = Account.class)
public interface UserProjection {
    Long getId();
    String getName();
    String getEmail();
}