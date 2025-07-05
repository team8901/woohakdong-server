package com.woohakdong.api.controller.dto.response;

import com.woohakdong.context.auth.model.SocialLoginTokens;

public record AuthSocialLoginResponse(
        String accessToken,
        String refreshToken
) {
    public static AuthSocialLoginResponse of(SocialLoginTokens socialLoginTokens) {
        return new AuthSocialLoginResponse(socialLoginTokens.accessToken(), socialLoginTokens.refreshToken());
    }
}
