package com.woohakdong.context.auth.model;

public record SocialLoginTokens(
        String accessToken,
        String refreshToken
) {
}
