package com.woohakdong.domain.auth.domain;

import com.woohakdong.domain.auth.model.AuthSocialLoginCommand;
import com.woohakdong.domain.auth.model.SocialUserInfo;
import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginDomainService {

    private final List<ExternalLoginProvider> providers;

    public SocialUserInfo resolveUserInfo(AuthSocialLoginCommand command) {
        return providers.stream()
                .filter(p -> p.supports(command.provider()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unsupported provider: " + command.provider()))
                .fetch(command.providerAccessToken());
    }
}
