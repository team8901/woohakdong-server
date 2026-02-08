package com.woohakdong.domain.auth.domain;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import com.woohakdong.domain.auth.model.SocialUserInfo;
import com.woohakdong.domain.auth.model.UserAuthRole;
import com.woohakdong.exception.CustomAuthException;
import com.woohakdong.exception.CustomErrorInfo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AdminAuthServiceTest {

    @Mock
    private FirebaseAuth firebaseAuth;

    @Mock
    private FirebaseToken firebaseToken;

    @InjectMocks
    private AdminAuthService adminAuthService;

    @Nested
    @DisplayName("verifyToken() 테스트")
    class VerifyTokenTest {

        @Test
        @DisplayName("정상적인 Email/Password 토큰 검증 성공")
        void verifyTokenSuccess() throws FirebaseAuthException {
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

            // when
            SocialUserInfo result = adminAuthService.verifyToken(idToken);

            // then
            assertThat(result).isNotNull();
            assertThat(result.email()).isEqualTo(email);
            assertThat(result.providerUserId()).isEqualTo(uid);
            assertThat(result.provider()).isEqualTo("email-password");
            assertThat(result.name()).isEqualTo("admin-12345678");
            assertThat(result.role()).isEqualTo(UserAuthRole.ADMIN);
        }

        @Test
        @DisplayName("Name 생성: UID 8자 이상 - 앞 8자만 사용")
        void generateAdminNameWith8OrMoreChars() throws FirebaseAuthException {
            // given
            String idToken = "valid-token";
            String uid = "123456789abcdef"; // 15자
            String email = "admin@test.com";

            Map<String, Object> firebaseData = new HashMap<>();
            firebaseData.put("sign_in_provider", "password");

            Map<String, Object> claims = new HashMap<>();
            claims.put("firebase", firebaseData);

            when(firebaseAuth.verifyIdToken(idToken)).thenReturn(firebaseToken);
            when(firebaseToken.getClaims()).thenReturn(claims);
            when(firebaseToken.getUid()).thenReturn(uid);
            when(firebaseToken.getEmail()).thenReturn(email);

            // when
            SocialUserInfo result = adminAuthService.verifyToken(idToken);

            // then
            assertThat(result.name()).isEqualTo("admin-12345678");
        }

        @Test
        @DisplayName("Name 생성: UID 8자 미만 - 전체 사용")
        void generateAdminNameWithLessThan8Chars() throws FirebaseAuthException {
            // given
            String idToken = "valid-token";
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

            // when
            SocialUserInfo result = adminAuthService.verifyToken(idToken);

            // then
            assertThat(result.name()).isEqualTo("admin-abc123");
        }

        @Test
        @DisplayName("잘못된 provider (google.com) - 예외 발생")
        void invalidProvider() throws FirebaseAuthException {
            // given
            String idToken = "invalid-provider-token";

            Map<String, Object> firebaseData = new HashMap<>();
            firebaseData.put("sign_in_provider", "google.com");

            Map<String, Object> claims = new HashMap<>();
            claims.put("firebase", firebaseData);

            when(firebaseAuth.verifyIdToken(idToken)).thenReturn(firebaseToken);
            when(firebaseToken.getClaims()).thenReturn(claims);

            // when & then
            assertThatThrownBy(() -> adminAuthService.verifyToken(idToken))
                    .isInstanceOf(CustomAuthException.class)
                    .satisfies(e -> {
                        CustomAuthException ex = (CustomAuthException) e;
                        assertThat(ex.getCustomErrorInfo())
                                .isEqualTo(CustomErrorInfo.BAD_REQUEST_INVALID_ADMIN_PROVIDER);
                    });
        }

        @Test
        @DisplayName("유효하지 않은 토큰 - BAD_REQUEST_FIREBASE_TOKEN 예외")
        void invalidToken() throws FirebaseAuthException {
            // given
            String idToken = "invalid-token";

            doThrow(FirebaseAuthException.class).when(firebaseAuth).verifyIdToken(anyString());

            // when & then
            assertThatThrownBy(() -> adminAuthService.verifyToken(idToken))
                    .isInstanceOf(CustomAuthException.class)
                    .satisfies(e -> {
                        CustomAuthException ex = (CustomAuthException) e;
                        assertThat(ex.getCustomErrorInfo())
                                .isEqualTo(CustomErrorInfo.BAD_REQUEST_FIREBASE_TOKEN);
                    });
        }

        @Test
        @DisplayName("Provider 변환: password -> email-password")
        void providerConversion() throws FirebaseAuthException {
            // given
            String idToken = "valid-token";
            String uid = "testuid";
            String email = "admin@test.com";

            Map<String, Object> firebaseData = new HashMap<>();
            firebaseData.put("sign_in_provider", "password");

            Map<String, Object> claims = new HashMap<>();
            claims.put("firebase", firebaseData);

            when(firebaseAuth.verifyIdToken(idToken)).thenReturn(firebaseToken);
            when(firebaseToken.getClaims()).thenReturn(claims);
            when(firebaseToken.getUid()).thenReturn(uid);
            when(firebaseToken.getEmail()).thenReturn(email);

            // when
            SocialUserInfo result = adminAuthService.verifyToken(idToken);

            // then
            assertThat(result.provider()).isEqualTo("email-password");
        }

        @Test
        @DisplayName("Name 생성: UID 정확히 8자 - 전체 사용")
        void generateAdminNameWithExactly8Chars() throws FirebaseAuthException {
            // given
            String idToken = "valid-token";
            String uid = "12345678"; // 정확히 8자
            String email = "admin@test.com";

            Map<String, Object> firebaseData = new HashMap<>();
            firebaseData.put("sign_in_provider", "password");

            Map<String, Object> claims = new HashMap<>();
            claims.put("firebase", firebaseData);

            when(firebaseAuth.verifyIdToken(idToken)).thenReturn(firebaseToken);
            when(firebaseToken.getClaims()).thenReturn(claims);
            when(firebaseToken.getUid()).thenReturn(uid);
            when(firebaseToken.getEmail()).thenReturn(email);

            // when
            SocialUserInfo result = adminAuthService.verifyToken(idToken);

            // then
            assertThat(result.name()).isEqualTo("admin-12345678");
        }
    }
}
