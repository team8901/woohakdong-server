package com.woohakdong.domain.auth.domain;

import com.woohakdong.domain.auth.model.SocialUserInfo;

public interface ExternalLoginProvider {
    boolean supports(String provider);

    SocialUserInfo fetch(String accessToken);
}
