package com.woohakdong.controller.dto.request;

import com.woohakdong.domain.auth.model.AuthSocialLoginCommand;
import jakarta.validation.constraints.AssertTrue;

public record AuthSocialLoginRequest(
        String provider,
        String providerAccessToken
) {
    public AuthSocialLoginCommand toCommandModel() {
        return new AuthSocialLoginCommand(provider, providerAccessToken);
    }

    @AssertTrue(message = "provider는 반드시 입력되어야합니다.")
    private boolean _isProviderValid() {
        return provider != null && !provider.isBlank();
    }

    @AssertTrue(message = "providerAccessToken는 반드시 입력되어야합니다.")
    private boolean _isProviderAccessTokenValid() {
        return providerAccessToken != null && !providerAccessToken.isBlank();
    }
}
