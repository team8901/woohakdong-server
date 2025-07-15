package com.woohakdong.domain.auth.model;

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
    private String name;
    private String email;
    @Enumerated(value = EnumType.STRING)
    private UserAuthRole role;
    private String authProvider;
    private String authProviderUserId;

    public static UserAuthEntity registerWithSocialLogin(SocialUserInfo socialUserInfo) {
        return new UserAuthEntity(
                null,
                socialUserInfo.name(),
                socialUserInfo.email(),
                UserAuthRole.USER,
                socialUserInfo.provider(),
                socialUserInfo.providerUserId()
        );
    }
}
