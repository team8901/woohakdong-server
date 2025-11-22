package com.woohakdong.api.dto.response;

import com.woohakdong.domain.club.model.ClubMemberRole;
import com.woohakdong.domain.club.model.ClubMembershipEntity;
import com.woohakdong.domain.user.model.Gender;

import java.time.LocalDate;

public record ClubMembershipResponse(
        Long clubMembershipId,
        String nickname,
        ClubMemberRole clubMemberRole,
        LocalDate clubJoinDate,
        String name,
        String email,
        String phoneNumber,
        String studentId,
        String major,
        Gender gender
) {
    public static ClubMembershipResponse of(ClubMembershipEntity membership) {
        return new ClubMembershipResponse(
                membership.getId(),
                membership.getNickname(),
                membership.getClubMemberRole(),
                membership.getClubJoinDate(),
                membership.getUserProfile().getName(),
                membership.getUserProfile().getEmail(),
                membership.getUserProfile().getPhoneNumber(),
                membership.getUserProfile().getStudentId(),
                membership.getUserProfile().getMajor(),
                membership.getUserProfile().getGender()
        );
    }
}
