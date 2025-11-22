package com.woohakdong.domain.club.infrastructure.storage;

import com.woohakdong.domain.club.model.ClubEntity;
import com.woohakdong.domain.club.model.ClubMembershipEntity;
import com.woohakdong.domain.user.model.UserProfileEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ClubMemberShipRepository extends JpaRepository<ClubMembershipEntity, Long> {
    boolean existsByClubAndUserProfile(ClubEntity club, UserProfileEntity userProfile);

    List<ClubMembershipEntity> findAllByUserProfile(UserProfileEntity userProfile);

    Optional<ClubMembershipEntity> findByUserProfile(UserProfileEntity userProfile);

    @EntityGraph(attributePaths = {"userProfile"})
    List<ClubMembershipEntity> findAllByClub(ClubEntity club);
}
