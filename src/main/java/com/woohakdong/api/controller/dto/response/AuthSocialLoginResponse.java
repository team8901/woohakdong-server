package com.woohakdong.api.controller.dto.response;

public record AuthSocialLoginResponse(
        String accessToken,
        String refreshToken
) {
}
