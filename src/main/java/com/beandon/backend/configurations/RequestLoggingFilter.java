package com.beandon.backend.configurations;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.AbstractRequestLoggingFilter;

import javax.servlet.http.HttpServletRequest;
import java.util.Set;

@Component
public class RequestLoggingFilter extends AbstractRequestLoggingFilter {

    private final Set<String> excludedUrls = Set.of("/health");

    @Override
    protected boolean shouldLog(HttpServletRequest request) {
        if(excludedUrls.contains(request.getRequestURI())) {
            return false;
        }
        return logger.isInfoEnabled();
    }

    @Override
    protected void beforeRequest(HttpServletRequest request, String message) {
        logger.info(message);
    }

    @Override
    protected void afterRequest(HttpServletRequest request, String message) {
        logger.info(message);
    }
}
