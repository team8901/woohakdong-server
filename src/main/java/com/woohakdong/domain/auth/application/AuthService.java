package com.woohakdong.domain.auth.application;

import com.woohakdong.domain.auth.domain.JwtTokenDomainService;
import com.woohakdong.domain.auth.domain.LoginDomainService;
import com.woohakdong.domain.auth.infrastructure.storage.repository.UserAuthRepository;
import com.woohakdong.domain.auth.model.AuthSocialLoginCommand;
import com.woohakdong.domain.auth.model.LoginTokens;
import com.woohakdong.domain.auth.model.SocialUserInfo;
import com.woohakdong.domain.auth.model.UserAuthEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final LoginDomainService loginDomainService;
    private final JwtTokenDomainService jwtTokenDomainService;

    private final UserAuthRepository userAuthRepository;

    @Transactional
    public LoginTokens socialLogin(AuthSocialLoginCommand socialLoginCommand) {
        SocialUserInfo socialUserInfo = loginDomainService.resolveUserInfo(socialLoginCommand);
        UserAuthEntity userAuth = userAuthRepository.findByAuthProviderAndAuthProviderUserId(socialUserInfo.provider(),
                socialUserInfo.providerUserId()).orElseGet(() -> {
                    // 소셜 로그인
                    UserAuthEntity newUser = UserAuthEntity.registerWithSocialLogin(socialUserInfo);
                    return userAuthRepository.save(newUser);
                }
        );
        return jwtTokenDomainService.publishLoginTokens(userAuth);
    }
}
