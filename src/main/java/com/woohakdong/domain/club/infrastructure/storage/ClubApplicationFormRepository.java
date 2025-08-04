package com.woohakdong.domain.club.infrastructure.storage;

import com.woohakdong.domain.club.model.ClubApplicationFormEntity;
import com.woohakdong.domain.club.model.ClubEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ClubApplicationFormRepository extends JpaRepository<ClubApplicationFormEntity, Long> {
     Optional<ClubApplicationFormEntity> findTopByClubOrderByCreatedAtDesc(ClubEntity club);

     List<ClubApplicationFormEntity> findAllByClubOrderByCreatedAtDesc(ClubEntity club);
}
