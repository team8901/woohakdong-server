package com.woohakdong.context.auth.domain;

import com.woohakdong.context.auth.model.SocialUserInfo;

public interface SocialLoginProvider {
    boolean supports(String provider);

    SocialUserInfo fetch(String accessToken);
}
