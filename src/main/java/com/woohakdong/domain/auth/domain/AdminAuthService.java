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
import static com.woohakdong.exception.CustomErrorInfo.BAD_REQUEST_INVALID_ADMIN_PROVIDER;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminAuthService {

    private final FirebaseAuth firebaseAuth;

    public SocialUserInfo verifyToken(String idToken) {
        try {
            FirebaseToken decodedToken = firebaseAuth.verifyIdToken(idToken);

            validateEmailPasswordProvider(decodedToken);

            String uid = decodedToken.getUid();
            String email = decodedToken.getEmail();
            String name = generateAdminName(uid);
            String provider = extractProvider(decodedToken);

            log.info("Admin Firebase token verified for user: {}", email);
            return new SocialUserInfo(name, email, uid, provider, UserAuthRole.ADMIN);
        } catch (FirebaseAuthException e) {
            log.error("Failed to verify Firebase token", e);
            throw new CustomAuthException(BAD_REQUEST_FIREBASE_TOKEN);
        }
    }

    private void validateEmailPasswordProvider(FirebaseToken token) {
        String provider = extractSignInProvider(token);
        if (!"password".equals(provider)) {
            throw new CustomAuthException(BAD_REQUEST_INVALID_ADMIN_PROVIDER);
        }
    }

    private String generateAdminName(String uid) {
        String uidPrefix = uid.length() >= 8 ? uid.substring(0, 8) : uid;
        return "admin-" + uidPrefix;
    }

    private String extractProvider(FirebaseToken token) {
        String provider = extractSignInProvider(token);
        if ("password".equals(provider)) {
            return "email-password";
        }
        return "firebase";
    }

    @SuppressWarnings("unchecked")
    private String extractSignInProvider(FirebaseToken token) {
        Object firebaseObj = token.getClaims().get("firebase");
        if (firebaseObj instanceof Map) {
            Map<String, Object> firebaseClaims = (Map<String, Object>) firebaseObj;
            Object providerObj = firebaseClaims.get("sign_in_provider");
            if (providerObj instanceof String) {
                return (String) providerObj;
            }
        }
        return null;
    }
}
