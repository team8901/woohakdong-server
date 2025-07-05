package com.woohakdong.context.auth.domain;

import com.woohakdong.context.auth.model.SocialLoginTokens;
import com.woohakdong.context.auth.model.UserAuthEntity;
import com.woohakdong.context.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JwtTokenService {

    private final JwtUtil jwtUtil;

    public SocialLoginTokens publishSocialLoginToken(UserAuthEntity userAuth) {
        Long userAuthId = userAuth.getId();
        String role = userAuth.getRole();

        String accessToken = jwtUtil.createToken("access", userAuthId, role, 600000L);
        String refreshToken = jwtUtil.createToken("refresh", userAuthId, role, 86400000L);

        // TODO : Refresh 저장

        return new SocialLoginTokens(accessToken, refreshToken);
    }
}
