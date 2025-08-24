package com.woohakdong.api.dto.response;

import com.woohakdong.domain.auth.model.AuthTokens;

public record AuthTokensDto(
        String accessToken,
        String refreshToken
) {
    public static AuthTokensDto of(AuthTokens authTokens) {
        return new AuthTokensDto(authTokens.accessToken(), authTokens.refreshToken());
    }
}
