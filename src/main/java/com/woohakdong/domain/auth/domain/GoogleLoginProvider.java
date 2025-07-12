package com.woohakdong.domain.auth.domain;

import com.woohakdong.domain.auth.infrastructure.client.GoogleOAuthClient;
import com.woohakdong.domain.auth.model.SocialUserInfo;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GoogleLoginProvider implements SocialLoginProvider {
    private static final String GOOGLE = "google";
    private static final String FIELD_EMAIL = "email";
    private static final String FIELD_NAME = "name";
    private static final String FIELD_SUB = "sub";

    private final GoogleOAuthClient oAuthClient;

    @Override
    public boolean supports(String provider) {
        return GOOGLE.equals(provider);
    }

    @Override
    public SocialUserInfo fetch(String accessToken) {
        Map<String, Object> body = oAuthClient.fetchGoogleOAuthPayload(accessToken);
        String name = extractField(body, FIELD_NAME);
        String email = extractField(body, FIELD_EMAIL);
        String providerUserId = extractField(body, FIELD_SUB);

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
