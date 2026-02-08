package com.woohakdong.domain.auth.domain;

import com.woohakdong.domain.auth.model.SocialUserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class AdminLoginProvider implements SocialLoginProvider {

    private static final Set<String> SUPPORTED_PROVIDERS = Set.of("email-password");

    private final AdminAuthService adminAuthService;

    @Override
    public boolean supports(String provider) {
        return SUPPORTED_PROVIDERS.contains(provider.toLowerCase());
    }

    @Override
    public SocialUserInfo fetch(String accessToken) {
        return adminAuthService.verifyToken(accessToken);
    }
}
