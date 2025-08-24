package com.woohakdong.api.dto.response;

import com.woohakdong.domain.auth.model.AuthTokens;

public record AuthSocialLoginResponse(
        String accessToken,
        String refreshToken
) {
    public static AuthSocialLoginResponse of(AuthTokens authTokens) {
        return new AuthSocialLoginResponse(authTokens.accessToken(), authTokens.refreshToken());
    }
}
