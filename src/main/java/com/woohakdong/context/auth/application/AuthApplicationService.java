package com.woohakdong.context.auth.application;

import com.woohakdong.context.auth.domain.AuthDomainService;
import com.woohakdong.context.auth.domain.JwtTokenService;
import com.woohakdong.context.auth.domain.SocialLoginService;
import com.woohakdong.context.auth.model.AuthSocialLoginCommand;
import com.woohakdong.context.auth.model.SocialLoginTokens;
import com.woohakdong.context.auth.model.SocialUserInfo;
import com.woohakdong.context.auth.model.UserAuthEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthApplicationService {

    private final SocialLoginService socialLoginService;
    private final AuthDomainService authDomainService;
    private final JwtTokenService jwtTokenService;

    public SocialLoginTokens socialLogin(AuthSocialLoginCommand socialLoginCommand) {
        SocialUserInfo socialUserInfo = socialLoginService.resolveUserInfo(socialLoginCommand);
        UserAuthEntity userAuth = authDomainService.createOrFindUser(socialUserInfo);
        return jwtTokenService.publishSocialLoginToken(userAuth);
    }
}
