package com.woohakdong.domain.auth.application;

import com.woohakdong.api.dto.response.AuthTokensDto;
import com.woohakdong.domain.auth.domain.AuthDomainService;
import com.woohakdong.domain.auth.domain.JwtTokenService;
import com.woohakdong.domain.auth.domain.SocialLoginService;
import com.woohakdong.domain.auth.model.AuthSocialLoginCommand;
import com.woohakdong.domain.auth.model.AuthTokens;
import com.woohakdong.domain.auth.model.SocialUserInfo;
import com.woohakdong.domain.auth.model.UserAuthEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final SocialLoginService socialLoginService;
    private final AuthDomainService authDomainService;
    private final JwtTokenService jwtTokenService;

    public AuthTokens socialLogin(AuthSocialLoginCommand socialLoginCommand) {
        SocialUserInfo socialUserInfo = socialLoginService.resolveUserInfo(socialLoginCommand);
        UserAuthEntity userAuth = authDomainService.createOrFindUser(socialUserInfo);
        return jwtTokenService.generateTokens(userAuth);
    }
}
