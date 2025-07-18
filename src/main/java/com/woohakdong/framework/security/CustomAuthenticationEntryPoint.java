package com.woohakdong.framework.security;

import static com.woohakdong.exception.CustomErrorInfo.UNAUTHORIZED_NO_TOKEN;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.woohakdong.exception.CustomAuthException;
import com.woohakdong.exception.CustomErrorInfo;
import com.woohakdong.exception.ErrorResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {
        CustomErrorInfo errorInfo = UNAUTHORIZED_NO_TOKEN;

        if (authException instanceof CustomAuthException customAuthException) {
            errorInfo = customAuthException.getCustomErrorInfo();
        }

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");

        String jsonResponse = objectMapper.writeValueAsString(
                ErrorResponse.of(
                        errorInfo.getMessage(),
                        errorInfo.getErrorCode()
                )
        );
        response.getWriter().write(jsonResponse);
    }
}
