package com.woohakdong.api.dto.response;

import com.woohakdong.domain.user.model.Gender;
import com.woohakdong.domain.user.model.UserProfileEntity;

public record UserProfileResponse(
        String name,
        String nickname,
        String email,
        String phoneNumber,
        String studentId,
        String major,
        Gender gender
) {
    public static UserProfileResponse from(UserProfileEntity userProfile) {
        return new UserProfileResponse(
                userProfile.getName(),
                userProfile.getNickname(),
                userProfile.getEmail(),
                userProfile.getPhoneNumber(),
                userProfile.getStudentId(),
                userProfile.getMajor(),
                userProfile.getGender()
        );
    }
}