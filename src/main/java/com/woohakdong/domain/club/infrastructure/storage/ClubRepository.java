package com.woohakdong.domain.club.infrastructure.storage;

import com.woohakdong.domain.club.model.ClubEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClubRepository extends JpaRepository<ClubEntity, Long> {
    boolean existsByNameOrNameEn(String name, String nameEn);
}
