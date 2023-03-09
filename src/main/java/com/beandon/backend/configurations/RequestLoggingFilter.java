package com.beandon.backend.configurations;

import com.beandon.backend.pojo.utility.RequestLog;
import com.beandon.backend.pojo.utility.ResponseLog;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Set;

@Slf4j
public class RequestLoggingFilter extends OncePerRequestFilter {
    private final Set<String> excludedUrls = Set.of("/health");
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper(request);
        ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(response);

        long startTime = System.currentTimeMillis();
        filterChain.doFilter(requestWrapper, responseWrapper);
        long endTime = System.currentTimeMillis();
        if(!excludedUrls.contains(request.getRequestURI())) {
            RequestLog requestLog = RequestLog.builder()
                    .method(request.getMethod())
                    .uri(request.getRequestURI())
                    .payload(getValue(requestWrapper.getContentAsByteArray(), request.getCharacterEncoding()))
                    .queryParams(request.getQueryString())
                    .build();

            ResponseLog responseLog = ResponseLog.builder()
                    .code(response.getStatus())
                    .payload(getValue(responseWrapper.getContentAsByteArray(), response.getCharacterEncoding()))
                    .executionTime(endTime - startTime)
                    .build();
            log.info("Request: {}", writeValue(objectMapper.writeValueAsString(requestLog)));
            log.info("Response: {}", writeValue(objectMapper.writeValueAsString(responseLog)));
        }
        responseWrapper.copyBodyToResponse();
    }

    private Object getValue(byte[] content, String encoding) {
        try {
            return objectMapper.readValue(content, Object.class);
        } catch (IOException e1) {
            try {
                return new String(content, 0, content.length, encoding);
            } catch (UnsupportedEncodingException e2) {
                e2.printStackTrace();
            }
        }
        return "";
    }

    private String writeValue(String value) {
        RequestMaskingFilter filter = new RequestMaskingFilter(value);
        return filter.removePasswords().toString();
    }
}
