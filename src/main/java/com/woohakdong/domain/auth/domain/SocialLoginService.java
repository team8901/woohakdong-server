package com.woohakdong.domain.auth.domain;

import com.woohakdong.domain.auth.model.AuthSocialLoginCommand;
import com.woohakdong.domain.auth.model.SocialUserInfo;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class SocialLoginService {

    private final List<SocialLoginProvider> providers;

    public SocialLoginService(List<SocialLoginProvider> providers) {
        this.providers = providers;
    }

    public SocialUserInfo resolveUserInfo(AuthSocialLoginCommand command) {
        return providers.stream()
                .filter(p -> p.supports(command.provider()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unsupported provider: " + command.provider()))
                .fetch(command.providerAccessToken());
    }
}
