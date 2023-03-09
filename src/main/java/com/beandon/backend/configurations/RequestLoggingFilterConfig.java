package com.beandon.backend.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.OncePerRequestFilter;

@Configuration
public class RequestLoggingFilterConfig {
    @Bean
    public OncePerRequestFilter requestLoggingFilter() {
        return new RequestLoggingFilter();
    }
}