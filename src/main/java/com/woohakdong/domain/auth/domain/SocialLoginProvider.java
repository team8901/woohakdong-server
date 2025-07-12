package com.woohakdong.domain.auth.domain;

import com.woohakdong.domain.auth.model.SocialUserInfo;

public interface SocialLoginProvider {
    boolean supports(String provider);

    SocialUserInfo fetch(String accessToken);
}
