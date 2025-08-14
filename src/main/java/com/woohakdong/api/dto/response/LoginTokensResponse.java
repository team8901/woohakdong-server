package com.woohakdong.api.dto.response;

import com.woohakdong.domain.auth.model.LoginTokens;

public record LoginTokensResponse(
        String accessToken,
        String refreshToken
) {
    public static LoginTokensResponse of(LoginTokens loginTokens) {
        return new LoginTokensResponse(loginTokens.accessToken(), loginTokens.refreshToken());
    }
}
