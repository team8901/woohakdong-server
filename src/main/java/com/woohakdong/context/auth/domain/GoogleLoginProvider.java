package com.woohakdong.context.auth.domain;

import com.woohakdong.context.auth.model.SocialUserInfo;
import java.util.Map;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Component
public class GoogleLoginProvider implements SocialLoginProvider {
    private static final String GOOGLE_OAUTH_ENDPOINT = "https://www.googleapis.com/oauth2/v3/userinfo";
    private static final String GOOGLE = "google";
    private static final String FIELD_EMAIL = "email";
    private static final String FIELD_NAME = "name";
    private static final String FIELD_SUB = "sub";

    private final RestTemplate restTemplate;

    public GoogleLoginProvider(RestTemplateBuilder builder) {
        this.restTemplate = builder.build();
    }

    @Override
    public boolean supports(String provider) {
        return GOOGLE.equals(provider);
    }

    @Override
    public SocialUserInfo fetch(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        HttpEntity<Void> entity = new HttpEntity<>(headers);
        ResponseEntity<Map<String, Object>> response = null;
        try {
            response = restTemplate.exchange(
                    GOOGLE_OAUTH_ENDPOINT,
                    HttpMethod.GET,
                    entity,
                    new ParameterizedTypeReference<>() {
                    }
            );
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode().value() == HttpStatus.UNAUTHORIZED.value()) {
                throw new IllegalStateException("Google OAuth 요청 실패: 인증되지 않은 토큰입니다.");
            }
            throw new IllegalStateException("Google OAuth 요청 실패: " + e.getResponseBodyAsString());
        }

        Map<String, Object> body = response.getBody();
        String name = extractField(body, FIELD_NAME);
        String email = extractField(body, FIELD_EMAIL);
        String providerUserId = extractField(body, FIELD_SUB);

        if (email == null || email.isBlank()) {
            throw new IllegalStateException("Google access token is invalid: email not found");
        }

        return new SocialUserInfo(name, email, providerUserId, GOOGLE);
    }

    private String extractField(Map<String, Object> body, String field) {
        if (body == null) {
            return null;
        }
        Object value = body.get(field);
        return (value instanceof String) ? (String) value : null;
    }

}
