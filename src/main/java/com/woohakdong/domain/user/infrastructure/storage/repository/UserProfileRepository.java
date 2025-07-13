package com.woohakdong.domain.user.infrastructure.storage.repository;

import com.woohakdong.domain.auth.model.UserAuthEntity;
import com.woohakdong.domain.user.model.UserProfileEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserProfileRepository extends JpaRepository<UserProfileEntity, Long> {
    Optional<UserProfileEntity> findByUserAuthEntity(UserAuthEntity userAuthEntity);
}
