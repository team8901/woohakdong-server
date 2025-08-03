package com.woohakdong.domain.club.infrastructure.storage;

import com.woohakdong.domain.club.model.ClubApplicationFormEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClubApplicationFormRepository extends JpaRepository<ClubApplicationFormEntity, Long> {
}
