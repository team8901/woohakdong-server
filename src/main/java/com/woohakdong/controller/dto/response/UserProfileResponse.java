package com.woohakdong.controller.dto.response;

import com.woohakdong.domain.user.model.Gender;
import com.woohakdong.domain.user.model.UserProfileEntity;

public record UserProfileResponse(
        String name,
        String nickname,
        String email,
        String phoneNumber,
        Gender gender
) {
    public static UserProfileResponse from(UserProfileEntity userProfile) {
        return new UserProfileResponse(
                userProfile.getName(),
                userProfile.getNickName(),
                userProfile.getEmail(),
                userProfile.getPhoneNumber(),
                userProfile.getGender()
        );
    }
}