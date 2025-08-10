package com.woohakdong.domain.club.infrastructure.storage;

import com.woohakdong.domain.club.model.ClubApplicationFormEntity;
import com.woohakdong.domain.club.model.ClubApplicationSubmissionEntity;
import com.woohakdong.domain.user.model.UserProfileEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClubApplicationSubmissionRepository extends JpaRepository<ClubApplicationSubmissionEntity,Long> {
    Optional<ClubApplicationSubmissionEntity> findByClubApplicationFormAndUserProfile(ClubApplicationFormEntity clubApplicationForm, UserProfileEntity userProfile);
}
