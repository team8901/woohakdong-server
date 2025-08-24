package com.woohakdong.api.facade;

import com.woohakdong.api.dto.request.AuthSocialLoginRequest;
import com.woohakdong.api.dto.response.AuthTokensDto;
import com.woohakdong.domain.auth.application.AuthService;
import com.woohakdong.domain.auth.domain.JwtTokenService;
import com.woohakdong.domain.auth.model.AuthSocialLoginCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthFacade {

    private final AuthService authService;
    private final JwtTokenService jwtTokenService;

    public AuthTokensDto socialLogin(AuthSocialLoginRequest request) {
        AuthSocialLoginCommand socialLoginCommand = request.toCommandModel();
        return AuthTokensDto.of(authService.socialLogin(socialLoginCommand));
    }

    public String refreshAccessToken(String refreshToken) {
        return jwtTokenService.refreshAccessToken(refreshToken);
    }
}
