package com.woohakdong.facade;

import com.woohakdong.controller.dto.request.AuthSocialLoginRequest;
import com.woohakdong.controller.dto.response.AuthSocialLoginResponse;
import com.woohakdong.domain.auth.application.AuthApplicationService;
import com.woohakdong.domain.auth.model.AuthSocialLoginCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthFacade {

    private final AuthApplicationService authApplicationService;

    public AuthSocialLoginResponse socialLogin(AuthSocialLoginRequest request) {
        AuthSocialLoginCommand socialLoginCommand = request.toCommandModel();
        return AuthSocialLoginResponse.of(authApplicationService.socialLogin(socialLoginCommand));
    }
}
