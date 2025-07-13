package com.woohakdong.facade;

import com.woohakdong.controller.dto.response.UserProfileResponse;
import com.woohakdong.domain.user.application.UserApplicationService;
import com.woohakdong.domain.user.model.UserProfileEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserFacade {

    private final UserApplicationService userApplicationService;

    public UserProfileResponse getProfileWithAuthId(Long userAuthId) {
        UserProfileEntity userProfile = userApplicationService.getProfileWithAuthId(userAuthId);
        return UserProfileResponse.from(userProfile);
    }
}
