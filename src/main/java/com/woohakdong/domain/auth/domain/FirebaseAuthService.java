package com.woohakdong.domain.auth.domain;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import com.woohakdong.domain.auth.model.SocialUserInfo;
import com.woohakdong.domain.auth.model.UserAuthRole;
import com.woohakdong.exception.CustomAuthException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

import static com.woohakdong.exception.CustomErrorInfo.BAD_REQUEST_FIREBASE_TOKEN;


/**
 * 현재 일반 유저 로그인 용으로 쓰이는 중.
 * 네이밍을 일반 유저/어드민 유저와 같은 방식으로 분리할 것도 고민 중.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class FirebaseAuthService {

    private final FirebaseAuth firebaseAuth;

    public SocialUserInfo verifyToken(String idToken) {
        try {
            FirebaseToken decodedToken = firebaseAuth.verifyIdToken(idToken);

            String uid = decodedToken.getUid();
            String email = decodedToken.getEmail();
            String name = decodedToken.getName();
            String provider = extractProvider(decodedToken);

            log.info("Firebase token verified for user: {}", email);
            return new SocialUserInfo(name, email, uid, provider, UserAuthRole.USER);
        } catch (FirebaseAuthException e) {
            log.error("Failed to verify Firebase token", e);
            throw new CustomAuthException(BAD_REQUEST_FIREBASE_TOKEN);
        }
    }

    @SuppressWarnings("unchecked")
    private String extractProvider(FirebaseToken token) {
        Object firebaseObj = token.getClaims().get("firebase");
        if (firebaseObj instanceof Map) {
            Map<String, Object> firebaseClaims = (Map<String, Object>) firebaseObj;
            Object providerObj = firebaseClaims.get("sign_in_provider");
            if (providerObj instanceof String) {
                String provider = (String) providerObj;
                // Firebase provider IDs: google.com, facebook.com, twitter.com, github.com, etc.
                return provider.replace(".com", "").toLowerCase();
            }
        }
        return "firebase";
    }
}