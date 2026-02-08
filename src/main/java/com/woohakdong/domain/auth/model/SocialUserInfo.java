package com.woohakdong.domain.auth.model;

public record SocialUserInfo(
        String name, // GOOGLE에는 존재
        String email,
        String providerUserId,
        String provider,
        UserAuthRole role // 소셜 로그인: USER, 관리자 로그인: ADMIN
) {
}
