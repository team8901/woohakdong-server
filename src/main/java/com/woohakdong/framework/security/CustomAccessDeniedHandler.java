package com.woohakdong.framework.security;

import static com.woohakdong.exception.CustomErrorInfo.FORBIDDEN_UNAUTHORIZED;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.woohakdong.exception.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    private final ObjectMapper objectMapper;

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");

        String jsonResponse = objectMapper.writeValueAsString(
                ErrorResponse.of(
                        FORBIDDEN_UNAUTHORIZED.getMessage(),
                        FORBIDDEN_UNAUTHORIZED.getErrorCode()
                )
        );
        response.getWriter().write(jsonResponse);
    }
}
