package com.woohakdong.domain.user.application;

import static com.woohakdong.framework.exception.CustomErrorInfo.CONFLICT_ALREADY_EXISTING_USER_PROFILE;
import static com.woohakdong.framework.exception.CustomErrorInfo.NOT_FOUND_USER_PROFILE;

import com.woohakdong.domain.auth.infrastructure.storage.repository.UserAuthRepository;
import com.woohakdong.domain.auth.model.UserAuthEntity;
import com.woohakdong.domain.user.infrastructure.storage.repository.UserProfileRepository;
import com.woohakdong.domain.user.model.UserProfileCreateCommand;
import com.woohakdong.domain.user.model.UserProfileEntity;
import com.woohakdong.framework.exception.CustomException;
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
                        () -> new CustomException(NOT_FOUND_USER_PROFILE)
                );
    }

    @Transactional
    public Long createProfileWithAuthId(Long userAuthId, UserProfileCreateCommand command) {
        UserAuthEntity userAuth = userAuthRepository.getReferenceById(userAuthId);
        if (userProfileRepository.existsByUserAuthEntity(userAuth)) {
            throw new CustomException(CONFLICT_ALREADY_EXISTING_USER_PROFILE);
        }
        UserProfileEntity userProfile = UserProfileEntity.createNewUser(userAuth, command);
        UserProfileEntity saved = userProfileRepository.save(userProfile);
        return saved.getId();
    }
}
