package com.woohakdong.domain.auth.application;

import com.woohakdong.domain.auth.domain.AuthDomainService;
import com.woohakdong.domain.auth.domain.JwtTokenService;
import com.woohakdong.domain.auth.domain.SocialLoginService;
import com.woohakdong.domain.auth.model.AuthSocialLoginCommand;
import com.woohakdong.domain.auth.model.SocialLoginTokens;
import com.woohakdong.domain.auth.model.SocialUserInfo;
import com.woohakdong.domain.auth.model.UserAuthEntity;
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
