package com.woohakdong.api.facade;

import com.woohakdong.api.dto.request.AuthSocialLoginRequest;
import com.woohakdong.api.dto.response.LoginTokensResponse;
import com.woohakdong.domain.auth.application.AuthService;
import com.woohakdong.domain.auth.model.AuthSocialLoginCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthFacade {

    private final AuthService authService;

    public LoginTokensResponse socialLogin(AuthSocialLoginRequest request) {
        AuthSocialLoginCommand socialLoginCommand = request.toCommandModel();
        return LoginTokensResponse.of(authService.socialLogin(socialLoginCommand));
    }
}
