package com.woohakdong.domain.auth.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
public class UserAuthEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    private String role;
    private String authProvider;
    private String authProviderUserId;

    public static UserAuthEntity registerWithSocialLogin(SocialUserInfo socialUserInfo) {
        return new UserAuthEntity(
                null,
                socialUserInfo.email(),
                "USER",
                socialUserInfo.provider(),
                socialUserInfo.providerUserId()
        );
    }
}
