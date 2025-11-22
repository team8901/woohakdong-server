package com.woohakdong.domain.user.model;

public record UserProfileCreateCommand(
        String nickname,
        String phoneNumber,
        String studentId,
        String major,
        Gender gender
) {
}
