package com.woohakdong.utils;

import com.woohakdong.domain.auth.model.UserAuthRole;
import com.woohakdong.exception.CustomAuthException;
import com.woohakdong.exception.CustomErrorInfo;
import com.woohakdong.framework.security.RequestUser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtUtil {

    private final SecretKey secretKey;

    private JwtUtil(@Value("${jwt.secret}") String secret) {
        this.secretKey = new SecretKeySpec(
                secret.getBytes(StandardCharsets.UTF_8),
                Jwts.SIG.HS256.key().build().getAlgorithm()
        );
    }

    /**
     * 토큰에서 userAuthId를 추출합니다. 내부적으로 토큰 유효성 검사를 수행합니다.
     *
     * @param token JWT 토큰
     * @return
     * @throws CustomAuthException 토큰이 유효하지 않은 경우
     */
    public RequestUser getRequestUserFromToken(String token) {
        Claims claims = getClaims(token);
        return new RequestUser(
                claims.get("userAuthId", Long.class),
                UserAuthRole.valueOf(claims.get("role", String.class))
        );
    }

    /**
     * 토큰의 타입을 반환합니다.
     *
     * @param token JWT 토큰
     * @return 토큰 타입 (access 또는 refresh)
     * @throws CustomAuthException 토큰이 유효하지 않은 경우
     */
    public String getTokenType(String token) {
        Claims claims = getClaims(token);
        return claims.get("type", String.class);
    }

    /**
     * 토큰을 파싱하여 클레임(Payload)을 반환하고, 과정에서 발생하는 예외를 처리합니다.
     *
     * @param token JWT 토큰
     * @return 토큰의 클레임
     * @throws CustomAuthException 토큰 관련 예외 발생 시
     */
    private Claims getClaims(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (ExpiredJwtException e) {
            // 토큰이 만료된 경우
            throw new CustomAuthException(CustomErrorInfo.UNAUTHORIZED_EXPIRED_TOKEN);
        } catch (JwtException | IllegalArgumentException e) {
            // 그 외 모든 JWT 관련 예외 (서명 오류, 형식 오류 등)
            throw new CustomAuthException(CustomErrorInfo.UNAUTHORIZED_INVALID_TOKEN);
        }
    }

    /**
     * 새로운 JWT 토큰을 생성합니다.
     */
    public String createToken(String type, Long userAuthId, UserAuthRole role, Long expiredMs) {
        return Jwts.builder()
                .claim("type", type)
                .claim("userAuthId", userAuthId)
                .claim("role", role)
                .expiration(new Date(System.currentTimeMillis() + expiredMs))
                .signWith(secretKey)
                .compact();
    }
}
