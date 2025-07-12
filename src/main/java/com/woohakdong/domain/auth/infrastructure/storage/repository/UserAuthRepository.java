package com.woohakdong.domain.auth.infrastructure.storage.repository;

import com.woohakdong.domain.auth.model.UserAuthEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserAuthRepository extends JpaRepository<UserAuthEntity, Long> {

    Optional<UserAuthEntity> findByAuthProviderAndAuthProviderUserId(String authProvider, String authProviderId);
}
