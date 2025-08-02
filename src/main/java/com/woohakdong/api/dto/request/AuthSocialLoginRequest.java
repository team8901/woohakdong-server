package com.woohakdong.api.dto.request;

import com.woohakdong.domain.auth.model.AuthSocialLoginCommand;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record AuthSocialLoginRequest(

        @Schema(example = "google", description = "소셜 로그인 제공자 (예: google, kakao, naver)")
        @NotBlank(message = "소셜 로그인 제공자는 필수입니다.")
        String provider,

        @Schema(example = "your-access-token", description = "소셜 로그인 제공자의 액세스 토큰")
        @NotBlank(message = "소셜 로그인 제공자의 액세스 토큰은 필수입니다.")
        String providerAccessToken
) {
    public AuthSocialLoginCommand toCommandModel() {
        return new AuthSocialLoginCommand(provider, providerAccessToken);
    }

}
