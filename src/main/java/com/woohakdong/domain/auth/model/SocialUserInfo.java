package com.woohakdong.domain.auth.model;

public record SocialUserInfo(
        String name, // GOOGLE에는 존재
        String email,
        String providerUserId,
        String provider
) {
}
