package com.woohakdong.domain.auth.domain;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import com.woohakdong.domain.auth.model.SocialUserInfo;
import com.woohakdong.exception.CustomAuthException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static com.woohakdong.exception.CustomErrorInfo.BAD_REQUEST_FIREBASE_TOKEN;

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
            return new SocialUserInfo(name, email, uid, provider);
        } catch (FirebaseAuthException e) {
            log.error("Failed to verify Firebase token", e);
            throw new CustomAuthException(BAD_REQUEST_FIREBASE_TOKEN);
        }
    }

    private String extractProvider(FirebaseToken token) {
        String provider = (String) token.getClaims().get("firebase.sign_in_provider");
        if (provider != null) {
            // Firebase provider IDs: google.com, facebook.com, twitter.com, github.com, etc.
            return provider.replace(".com", "").toLowerCase();
        }
        return "firebase";
    }
}