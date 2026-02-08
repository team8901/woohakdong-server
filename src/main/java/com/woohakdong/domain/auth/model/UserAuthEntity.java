package com.woohakdong.domain.auth.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Table(name = "user_auth")
public class UserAuthEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "role", nullable = false)
    @Enumerated(value = EnumType.STRING)
    private UserAuthRole role;

    @Column(name = "auth_provider", nullable = false)
    private String authProvider;

    @Column(name = "auth_provider_user_id", nullable = false, unique = true)
    private String authProviderUserId;

    public static UserAuthEntity registerWithSocialLogin(SocialUserInfo socialUserInfo) {
        return new UserAuthEntity(
                null,
                socialUserInfo.name(),
                socialUserInfo.email(),
                socialUserInfo.role(),
                socialUserInfo.provider(),
                socialUserInfo.providerUserId()
        );
    }
}
