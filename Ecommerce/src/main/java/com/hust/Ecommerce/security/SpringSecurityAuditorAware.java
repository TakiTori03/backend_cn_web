package com.hust.Ecommerce.security;

import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import com.hust.Ecommerce.constants.AppConstants;

import java.util.Optional;

@Component
public class SpringSecurityAuditorAware implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
        return Optional.of(SecurityUtils.getCurrentUserLogin().orElse(AppConstants.SYSTEM));
    }
}
