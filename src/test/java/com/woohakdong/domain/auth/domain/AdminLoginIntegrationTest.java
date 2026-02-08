package com.woohakdong.domain.auth.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import com.woohakdong.domain.auth.model.AuthSocialLoginCommand;
import com.woohakdong.domain.auth.model.SocialUserInfo;
import com.woohakdong.domain.auth.model.UserAuthRole;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class AdminLoginIntegrationTest {

    @Autowired
    private SocialLoginService socialLoginService;

    @MockitoBean
    private FirebaseAuth firebaseAuth;

    @MockitoBean
    private FirebaseToken firebaseToken;

    @Nested
    @DisplayName("SocialLoginService Integration 테스트")
    class SocialLoginServiceIntegrationTest {

        @Test
        @DisplayName("email-password provider에 대해 AdminLoginProvider를 자동으로 선택")
        void selectAdminLoginProviderForEmailPassword() throws FirebaseAuthException {
            // given
            String idToken = "valid-admin-token";
            String uid = "12345678abcd";
            String email = "admin@test.com";

            Map<String, Object> firebaseData = new HashMap<>();
            firebaseData.put("sign_in_provider", "password");

            Map<String, Object> claims = new HashMap<>();
            claims.put("firebase", firebaseData);

            when(firebaseAuth.verifyIdToken(idToken)).thenReturn(firebaseToken);
            when(firebaseToken.getClaims()).thenReturn(claims);
            when(firebaseToken.getUid()).thenReturn(uid);
            when(firebaseToken.getEmail()).thenReturn(email);

            AuthSocialLoginCommand command = new AuthSocialLoginCommand("email-password", idToken);

            // when
            SocialUserInfo result = socialLoginService.resolveUserInfo(command);

            // then
            assertThat(result).isNotNull();
            assertThat(result.provider()).isEqualTo("email-password");
            assertThat(result.name()).isEqualTo("admin-12345678");
            assertThat(result.email()).isEqualTo(email);
            assertThat(result.providerUserId()).isEqualTo(uid);
            assertThat(result.role()).isEqualTo(UserAuthRole.ADMIN);
        }

        @Test
        @DisplayName("End-to-end 흐름: provider=email-password -> AdminLoginProvider -> AdminAuthService -> SocialUserInfo")
        void endToEndFlow() throws FirebaseAuthException {
            // given
            String idToken = "admin-token";
            String uid = "abcd1234efgh";
            String email = "test.admin@woohakdong.com";

            Map<String, Object> firebaseData = new HashMap<>();
            firebaseData.put("sign_in_provider", "password");

            Map<String, Object> claims = new HashMap<>();
            claims.put("firebase", firebaseData);

            when(firebaseAuth.verifyIdToken(idToken)).thenReturn(firebaseToken);
            when(firebaseToken.getClaims()).thenReturn(claims);
            when(firebaseToken.getUid()).thenReturn(uid);
            when(firebaseToken.getEmail()).thenReturn(email);

            AuthSocialLoginCommand command = new AuthSocialLoginCommand("email-password", idToken);

            // when
            SocialUserInfo result = socialLoginService.resolveUserInfo(command);

            // then
            assertThat(result.name()).isEqualTo("admin-abcd1234");
            assertThat(result.email()).isEqualTo(email);
            assertThat(result.providerUserId()).isEqualTo(uid);
            assertThat(result.provider()).isEqualTo("email-password");
            assertThat(result.role()).isEqualTo(UserAuthRole.ADMIN);
        }

        @Test
        @DisplayName("Case insensitive provider 선택: Email-Password")
        void caseInsensitiveProviderSelection() throws FirebaseAuthException {
            // given
            String idToken = "admin-token";
            String uid = "test1234";
            String email = "admin@test.com";

            Map<String, Object> firebaseData = new HashMap<>();
            firebaseData.put("sign_in_provider", "password");

            Map<String, Object> claims = new HashMap<>();
            claims.put("firebase", firebaseData);

            when(firebaseAuth.verifyIdToken(idToken)).thenReturn(firebaseToken);
            when(firebaseToken.getClaims()).thenReturn(claims);
            when(firebaseToken.getUid()).thenReturn(uid);
            when(firebaseToken.getEmail()).thenReturn(email);

            AuthSocialLoginCommand command = new AuthSocialLoginCommand("Email-Password", idToken);

            // when
            SocialUserInfo result = socialLoginService.resolveUserInfo(command);

            // then
            assertThat(result).isNotNull();
            assertThat(result.provider()).isEqualTo("email-password");
            assertThat(result.role()).isEqualTo(UserAuthRole.ADMIN);
        }

        @Test
        @DisplayName("UID가 8자 미만인 경우 전체 UID 사용")
        void shortUidHandling() throws FirebaseAuthException {
            // given
            String idToken = "admin-token";
            String uid = "abc123"; // 6자
            String email = "admin@test.com";

            Map<String, Object> firebaseData = new HashMap<>();
            firebaseData.put("sign_in_provider", "password");

            Map<String, Object> claims = new HashMap<>();
            claims.put("firebase", firebaseData);

            when(firebaseAuth.verifyIdToken(idToken)).thenReturn(firebaseToken);
            when(firebaseToken.getClaims()).thenReturn(claims);
            when(firebaseToken.getUid()).thenReturn(uid);
            when(firebaseToken.getEmail()).thenReturn(email);

            AuthSocialLoginCommand command = new AuthSocialLoginCommand("email-password", idToken);

            // when
            SocialUserInfo result = socialLoginService.resolveUserInfo(command);

            // then
            assertThat(result.name()).isEqualTo("admin-abc123");
        }

        @Test
        @DisplayName("google provider는 FirebaseLoginProvider가 처리")
        void googleProviderUseFirebaseLoginProvider() throws FirebaseAuthException {
            // given
            String idToken = "google-token";
            String uid = "google-uid-123";
            String email = "user@gmail.com";
            String name = "Google User";

            Map<String, Object> firebaseData = new HashMap<>();
            firebaseData.put("sign_in_provider", "google.com");

            Map<String, Object> claims = new HashMap<>();
            claims.put("firebase", firebaseData);

            when(firebaseAuth.verifyIdToken(idToken)).thenReturn(firebaseToken);
            when(firebaseToken.getClaims()).thenReturn(claims);
            when(firebaseToken.getUid()).thenReturn(uid);
            when(firebaseToken.getEmail()).thenReturn(email);
            when(firebaseToken.getName()).thenReturn(name);

            AuthSocialLoginCommand command = new AuthSocialLoginCommand("google", idToken);

            // when
            SocialUserInfo result = socialLoginService.resolveUserInfo(command);

            // then
            assertThat(result.provider()).isEqualTo("google");
            assertThat(result.name()).isEqualTo(name);
        }

        @Test
        @DisplayName("지원하지 않는 provider는 예외 발생")
        void unsupportedProviderThrowsException() {
            // given
            AuthSocialLoginCommand command = new AuthSocialLoginCommand("unsupported-provider", "token");

            // when & then
            assertThatThrownBy(() -> socialLoginService.resolveUserInfo(command))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("Unsupported provider");
        }
    }
}
