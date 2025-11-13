package com.woohakdong.framework.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Enumeration;

@Slf4j
@Component
public class RequestLoggingFilter extends OncePerRequestFilter {

    private static final int MAX_PAYLOAD_LENGTH = 10000; // 최대 10KB까지만 로깅

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // Request/Response를 캐싱 가능한 래퍼로 감싸기
        ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper(request);
        ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(response);

        long startTime = System.currentTimeMillis();

        try {
            // 다음 필터 체인 실행
            filterChain.doFilter(requestWrapper, responseWrapper);
        } finally {
            long duration = System.currentTimeMillis() - startTime;

            // 요청/응답 로깅
            logRequest(requestWrapper, duration);
            logResponse(responseWrapper, duration);

            // Response body를 실제 응답에 복사 (중요!)
            responseWrapper.copyBodyToResponse();
        }
    }

    private void logRequest(ContentCachingRequestWrapper request, long duration) {
        StringBuilder logMessage = new StringBuilder("\n");
        logMessage.append("============================== HTTP Request ==============================\n");
        logMessage.append("Method       : ").append(request.getMethod()).append("\n");
        logMessage.append("URI          : ").append(request.getRequestURI()).append("\n");

        if (request.getQueryString() != null) {
            logMessage.append("Query String : ").append(request.getQueryString()).append("\n");
        }

        // Headers 로깅
        logMessage.append("Headers      :\n");
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            String headerValue = request.getHeader(headerName);
            logMessage.append("  ").append(headerName).append(": ").append(headerValue).append("\n");
        }

        // Cookies 로깅
        Cookie[] cookies = request.getCookies();
        if (cookies != null && cookies.length > 0) {
            logMessage.append("Cookies      :\n");
            Arrays.stream(cookies).forEach(cookie ->
                logMessage.append("  ").append(cookie.getName())
                          .append("=").append(cookie.getValue()).append("\n")
            );
        }

        // Request Body 로깅
        byte[] content = request.getContentAsByteArray();
        if (content.length > 0) {
            String body = new String(content, StandardCharsets.UTF_8);
            if (body.length() > MAX_PAYLOAD_LENGTH) {
                body = body.substring(0, MAX_PAYLOAD_LENGTH) + "... (truncated)";
            }
            logMessage.append("Request Body :\n").append(body).append("\n");
        }

        logMessage.append("Duration     : ").append(duration).append("ms\n");
        logMessage.append("==========================================================================");

        log.info(logMessage.toString());
    }

    private void logResponse(ContentCachingResponseWrapper response, long duration) {
        StringBuilder logMessage = new StringBuilder("\n");
        logMessage.append("============================== HTTP Response =============================\n");
        logMessage.append("Status       : ").append(response.getStatus()).append("\n");

        // Response Headers 로깅
        logMessage.append("Headers      :\n");
        response.getHeaderNames().forEach(headerName -> {
            String headerValue = response.getHeader(headerName);
            logMessage.append("  ").append(headerName).append(": ").append(headerValue).append("\n");
        });

        // Response Body 로깅
        byte[] content = response.getContentAsByteArray();
        if (content.length > 0) {
            String body = new String(content, StandardCharsets.UTF_8);
            if (body.length() > MAX_PAYLOAD_LENGTH) {
                body = body.substring(0, MAX_PAYLOAD_LENGTH) + "... (truncated)";
            }
            logMessage.append("Response Body:\n").append(body).append("\n");
        }

        logMessage.append("Duration     : ").append(duration).append("ms\n");
        logMessage.append("==========================================================================");

        log.info(logMessage.toString());
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        // Swagger UI 요청은 로깅하지 않음
        String path = request.getRequestURI();
        return path.startsWith("/swagger") ||
               path.startsWith("/v3/api-docs") ||
               path.startsWith("/swagger-ui") ||
               path.startsWith("/swagger-resources");
    }
}
