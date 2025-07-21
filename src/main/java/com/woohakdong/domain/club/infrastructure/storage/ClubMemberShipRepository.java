package com.woohakdong.domain.club.infrastructure.storage;

import com.woohakdong.domain.club.model.ClubEntity;
import com.woohakdong.domain.club.model.ClubMembershipEntity;
import com.woohakdong.domain.user.model.UserProfileEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClubMemberShipRepository extends JpaRepository<ClubMembershipEntity, Long> {
    boolean existsByClubAndUserProfile(ClubEntity club, UserProfileEntity userProfile);

    List<ClubMembershipEntity> findAllByUserProfile(UserProfileEntity userProfile);
}
