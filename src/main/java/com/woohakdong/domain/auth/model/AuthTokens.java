package com.woohakdong.domain.auth.model;

public record AuthTokens(
        String accessToken,
        String refreshToken
) {
}
