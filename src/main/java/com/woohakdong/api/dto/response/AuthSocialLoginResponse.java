package com.woohakdong.api.dto.response;

import com.woohakdong.domain.auth.model.SocialLoginTokens;

public record AuthSocialLoginResponse(
        String accessToken,
        String refreshToken
) {
    public static AuthSocialLoginResponse of(SocialLoginTokens socialLoginTokens) {
        return new AuthSocialLoginResponse(socialLoginTokens.accessToken(), socialLoginTokens.refreshToken());
    }
}
