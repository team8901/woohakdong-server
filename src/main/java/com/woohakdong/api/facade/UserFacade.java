package com.woohakdong.api.facade;

import com.woohakdong.api.dto.request.UserProfileCreateRequest;
import com.woohakdong.api.dto.response.UserProfileIdResponse;
import com.woohakdong.api.dto.response.UserProfileResponse;
import com.woohakdong.domain.user.application.UserApplicationService;
import com.woohakdong.domain.user.model.UserProfileCreateCommand;
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

    public UserProfileIdResponse createProfileWithAuthId(Long userAuthId, UserProfileCreateRequest request) {
        UserProfileCreateCommand command = request.toCommandModel();
        Long userProfileId = userApplicationService.createProfileWithAuthId(userAuthId, command);
        return UserProfileIdResponse.from(userProfileId);
    }
}
