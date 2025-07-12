package com.woohakdong.domain.auth.domain;

import com.woohakdong.domain.auth.infrastructure.storage.repository.UserAuthRepository;
import com.woohakdong.domain.auth.model.SocialUserInfo;
import com.woohakdong.domain.auth.model.UserAuthEntity;
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
