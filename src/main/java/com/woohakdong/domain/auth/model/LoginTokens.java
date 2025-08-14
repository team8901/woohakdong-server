package com.woohakdong.domain.auth.model;

public record LoginTokens(
        String accessToken,
        String refreshToken
) {
}
