package com.woohakdong.domain.auth.infrastructure.client;

import java.util.Map;
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
public class GoogleOAuthClient {
    private static final String GOOGLE_OAUTH_ENDPOINT = "https://www.googleapis.com/oauth2/v3/userinfo";
    private final RestTemplate restTemplate = new RestTemplate();

    public Map<String, Object> fetchGoogleOAuthPayload(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        HttpEntity<Void> entity = new HttpEntity<>(headers);
        ResponseEntity<Map<String, Object>> response;
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
        return response.getBody();
    }
}
