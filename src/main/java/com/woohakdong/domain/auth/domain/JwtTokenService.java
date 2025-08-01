package com.woohakdong.domain.auth.domain;

import com.woohakdong.domain.auth.model.SocialLoginTokens;
import com.woohakdong.domain.auth.model.UserAuthEntity;
import com.woohakdong.domain.auth.model.UserAuthRole;
import com.woohakdong.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JwtTokenService {

    private final JwtUtil jwtUtil;

    public SocialLoginTokens publishSocialLoginToken(UserAuthEntity userAuth) {
        Long userAuthId = userAuth.getId();
        UserAuthRole userAuthRole = userAuth.getRole();

        String accessToken = jwtUtil.createToken("access", userAuthId, userAuthRole, 60000000000000L);
        String refreshToken = jwtUtil.createToken("refresh", userAuthId, userAuthRole, 86400000L);

        // TODO : Refresh 저장

        return new SocialLoginTokens(accessToken, refreshToken);
    }
}
