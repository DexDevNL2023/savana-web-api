package com.savana.auth.generic.config;

import com.savana.auth.security.SecurityUtils;
import com.savana.auth.generic.utils.AppConstants;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
class SpringSecurityAuditAwareImpl implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
        return Optional.of(SecurityUtils.getCurrentUserLogin().orElse(AppConstants.SYSTEM));
    }
}