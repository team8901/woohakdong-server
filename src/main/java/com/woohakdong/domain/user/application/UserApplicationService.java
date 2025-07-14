package com.woohakdong.domain.user.application;

import com.woohakdong.domain.auth.infrastructure.storage.repository.UserAuthRepository;
import com.woohakdong.domain.auth.model.UserAuthEntity;
import com.woohakdong.domain.user.infrastructure.storage.repository.UserProfileRepository;
import com.woohakdong.domain.user.model.UserProfileCreateCommand;
import com.woohakdong.domain.user.model.UserProfileEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserApplicationService {
    private final UserProfileRepository userProfileRepository;
    private final UserAuthRepository userAuthRepository;

    public UserProfileEntity getProfileWithAuthId(Long userAuthId) {
        UserAuthEntity userAuth = userAuthRepository.getReferenceById(userAuthId);
        return userProfileRepository.findByUserAuthEntity(userAuth)
                .orElseThrow(
                        // TODO : 커스텀 에러로 변경
                        () -> new IllegalArgumentException("프로필을 입력하지 않았습니다.")
                );
    }

    @Transactional
    public Long createProfileWithAuthId(Long userAuthId, UserProfileCreateCommand command) {
        UserAuthEntity userAuth = userAuthRepository.getReferenceById(userAuthId);
        UserProfileEntity userProfile = UserProfileEntity.createNewUser(userAuth, command);
        UserProfileEntity saved = userProfileRepository.save(userProfile);
        return saved.getId();
    }
}
