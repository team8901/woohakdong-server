package com.woohakdong.domain.club.infrastructure.storage;

import com.woohakdong.domain.club.model.ClubApplicationSubmissionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClubApplicationSubmissionRepository extends JpaRepository<ClubApplicationSubmissionEntity,Long> {
}
