package com.woohakdong.domain.auth.domain;

import com.woohakdong.domain.auth.infrastructure.storage.repository.RefreshTokenRepository;
import com.woohakdong.domain.auth.model.RefreshTokenEntity;
import com.woohakdong.domain.auth.model.AuthTokens;
import com.woohakdong.domain.auth.model.UserAuthEntity;
import com.woohakdong.domain.auth.model.UserAuthRole;
import com.woohakdong.exception.CustomAuthException;
import com.woohakdong.exception.CustomErrorInfo;
import com.woohakdong.framework.security.RequestUser;
import com.woohakdong.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class JwtTokenService {

    private final JwtUtil jwtUtil;
    private final RefreshTokenRepository refreshTokenRepository;

    private static final long ACCESS_TOKEN_VALIDITY = 60 * 60 * 1000L; // 1시간 (임시 값)
    private static final long REFRESH_TOKEN_VALIDITY = 7 * 24 * 60 * 60 * 1000L; // 7일

    @Transactional
    public AuthTokens generateTokens(UserAuthEntity userAuth) {
        Long userAuthId = userAuth.getId();
        UserAuthRole userAuthRole = userAuth.getRole();

        // Access Token 생성
        String accessToken = jwtUtil.createToken("access", userAuthId, userAuthRole, ACCESS_TOKEN_VALIDITY);
        
        // 기존 refresh token 삭제
        refreshTokenRepository.deleteByUserAuthId(userAuthId);

        // 새로운 refresh token 생성
        String refreshToken = jwtUtil.createToken("refresh", userAuthId, userAuthRole, REFRESH_TOKEN_VALIDITY);

        // DB에 저장
        LocalDateTime expiredAt = LocalDateTime.now().plusDays(7);
        RefreshTokenEntity refreshTokenEntity = RefreshTokenEntity.create(userAuthId, refreshToken, expiredAt);
        refreshTokenRepository.save(refreshTokenEntity);

        return new AuthTokens(accessToken, refreshToken);
    }

    @Transactional
    public String refreshAccessToken(String refreshTokenStr) {
        // Refresh token 검증 및 정보 추출
        RequestUser requestUser = jwtUtil.getRequestUserFromToken(refreshTokenStr);
        
        // Token type 검증 (refresh token인지 확인)
        String tokenType = jwtUtil.getTokenType(refreshTokenStr);
        if (!"refresh".equals(tokenType)) {
            throw new CustomAuthException(CustomErrorInfo.UNAUTHORIZED_INVALID_TOKEN);
        }
        
        // DB에서 refresh token 확인
        RefreshTokenEntity refreshToken = refreshTokenRepository.findByToken(refreshTokenStr)
                .orElseThrow(() -> new CustomAuthException(CustomErrorInfo.UNAUTHORIZED_INVALID_TOKEN));

        // 만료 확인
        if (refreshToken.isExpired()) {
            refreshTokenRepository.delete(refreshToken);
            throw new CustomAuthException(CustomErrorInfo.UNAUTHORIZED_EXPIRED_TOKEN);
        }

        // 새로운 access token 발급
        return jwtUtil.createToken("access", requestUser.getUserAuthId(), requestUser.getUserAuthRole(), ACCESS_TOKEN_VALIDITY);
    }

    @Transactional
    public void revokeRefreshToken(Long userAuthId) {
        refreshTokenRepository.deleteByUserAuthId(userAuthId);
    }
}
