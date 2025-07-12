package com.woohakdong.domain.auth.model;

public record SocialLoginTokens(
        String accessToken,
        String refreshToken
) {
}
