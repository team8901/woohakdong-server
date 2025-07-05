package com.woohakdong.context.auth.domain;

import com.woohakdong.context.auth.model.SocialLoginTokens;
import com.woohakdong.context.auth.model.UserAuthEntity;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class JwtTokenService {

    private final SecretKey secretKey;

    public JwtTokenService(@Value("${jwt.secret}") String secret) {
        secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8),
                Jwts.SIG.HS256.key().build().getAlgorithm());
    }

    public SocialLoginTokens publishSocialLoginToken(UserAuthEntity userAuth) {
        Long userAuthId = userAuth.getId();
        String role = userAuth.getRole();

        String accessToken = createToken("access", userAuthId, role, 600000L);
        String refreshToken = createToken("refresh", userAuthId, role, 86400000L);

        // TODO : Refresh 저장

        return new SocialLoginTokens(accessToken, refreshToken);
    }

    public boolean validateToken(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();

        return !claims.getExpiration().before(new Date());
    }


    private String createToken(String type, Long userAuthId, String role, Long expiredMs) {
        return Jwts.builder()
                .claim("type", type)
                .claim("userAuthId", userAuthId)
                .claim("role", role)
                .expiration(new Date(System.currentTimeMillis() + expiredMs))
                .signWith(secretKey)
                .compact();
    }

    public Long getUserAuthIdFromToken(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();

        return claims.get("userAuthId", Long.class);
    }
}
