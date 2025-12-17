package com.woohakdong.domain.club.model;

import com.woohakdong.domain.user.model.UserProfileEntity;
import jakarta.persistence.*;

import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Table(name = "club_membership")
public class ClubMembershipEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDate clubJoinDate; // 가입일

    @Enumerated(EnumType.STRING)
    private ClubMemberRole clubMemberRole;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_profile_id")
    private UserProfileEntity userProfile;

    @Column(nullable = false)
    private String nickname;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "club_id")
    private ClubEntity club;

    public static ClubMembershipEntity createClubOwner(ClubEntity club, UserProfileEntity userProfile) {
        return new ClubMembershipEntity(
                null,
                LocalDate.now(),
                ClubMemberRole.PRESIDENT,
                userProfile,
                userProfile.getNickname(),
                club
        );
    }

    public static ClubMembershipEntity createMember(ClubEntity club, UserProfileEntity userProfile) {
        return new ClubMembershipEntity(
                null,
                LocalDate.now(),
                ClubMemberRole.MEMBER,
                userProfile,
                userProfile.getNickname(),
                club
        );
    }
}
