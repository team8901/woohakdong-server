package com.woohakdong.domain.auth.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.woohakdong.domain.auth.model.SocialUserInfo;
import com.woohakdong.domain.auth.model.UserAuthRole;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AdminLoginProviderTest {

    @Mock
    private AdminAuthService adminAuthService;

    @InjectMocks
    private AdminLoginProvider adminLoginProvider;

    @Nested
    @DisplayName("supports() 테스트")
    class SupportsTest {

        @Test
        @DisplayName("email-password provider 지원 - true 반환")
        void supportsEmailPassword() {
            // when & then
            assertThat(adminLoginProvider.supports("email-password")).isTrue();
        }

        @Test
        @DisplayName("google provider 미지원 - false 반환")
        void doesNotSupportGoogle() {
            // when & then
            assertThat(adminLoginProvider.supports("google")).isFalse();
        }

        @Test
        @DisplayName("Case insensitivity - Email-Password도 지원")
        void supportsCaseInsensitive() {
            // when & then
            assertThat(adminLoginProvider.supports("Email-Password")).isTrue();
            assertThat(adminLoginProvider.supports("EMAIL-PASSWORD")).isTrue();
            assertThat(adminLoginProvider.supports("eMaIl-PaSsWoRd")).isTrue();
        }

        @Test
        @DisplayName("facebook provider 미지원 - false 반환")
        void doesNotSupportFacebook() {
            // when & then
            assertThat(adminLoginProvider.supports("facebook")).isFalse();
        }

        @Test
        @DisplayName("firebase provider 미지원 - false 반환")
        void doesNotSupportFirebase() {
            // when & then
            assertThat(adminLoginProvider.supports("firebase")).isFalse();
        }
    }

    @Nested
    @DisplayName("fetch() 테스트")
    class FetchTest {

        @Test
        @DisplayName("fetch() 메서드가 AdminAuthService.verifyToken() 호출")
        void fetchCallsAdminAuthService() {
            // given
            String accessToken = "valid-token";
            SocialUserInfo expectedUserInfo = new SocialUserInfo(
                    "admin-12345678",
                    "admin@test.com",
                    "12345678abcd",
                    "email-password",
                    UserAuthRole.ADMIN
            );

            when(adminAuthService.verifyToken(accessToken)).thenReturn(expectedUserInfo);

            // when
            SocialUserInfo result = adminLoginProvider.fetch(accessToken);

            // then
            verify(adminAuthService).verifyToken(accessToken);
            assertThat(result).isEqualTo(expectedUserInfo);
            assertThat(result.name()).isEqualTo("admin-12345678");
            assertThat(result.email()).isEqualTo("admin@test.com");
            assertThat(result.providerUserId()).isEqualTo("12345678abcd");
            assertThat(result.provider()).isEqualTo("email-password");
            assertThat(result.role()).isEqualTo(UserAuthRole.ADMIN);
        }
    }
}
