package com.woohakdong.domain.auth.domain;

import com.woohakdong.domain.auth.model.SocialUserInfo;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FirebaseLoginProvider implements SocialLoginProvider {
    
    private static final Set<String> SUPPORTED_PROVIDERS = Set.of(
            "google", "facebook", "twitter", "github", 
            "kakao", "naver", "apple", "firebase"
    );
    
    private final FirebaseAuthService firebaseAuthService;

    @Override
    public boolean supports(String provider) {
        return SUPPORTED_PROVIDERS.contains(provider.toLowerCase());
    }

    @Override
    public SocialUserInfo fetch(String accessToken) {
        // With Firebase, we expect an ID token, not an access token
        return firebaseAuthService.verifyToken(accessToken);
    }
}