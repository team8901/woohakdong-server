package com.woohakdong.context.auth.domain;

import com.woohakdong.context.auth.infrastructure.storage.repository.UserAuthRepository;
import com.woohakdong.context.auth.model.SocialUserInfo;
import com.woohakdong.context.auth.model.UserAuthEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthDomainService {

    private final UserAuthRepository userAuthRepository;

    @Transactional
    public UserAuthEntity createOrFindUser(SocialUserInfo socialUserInfo) {
        return userAuthRepository.findByAuthProviderAndAuthProviderUserId(socialUserInfo.provider(),
                socialUserInfo.providerUserId()
        ).orElseGet(() -> {
            UserAuthEntity newUser = UserAuthEntity.registerWithSocialLogin(socialUserInfo);
            return userAuthRepository.save(newUser);
        });
    }
}
