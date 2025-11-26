package com.woohakdong.api.controller;

import com.woohakdong.api.dto.request.AuthSocialLoginRequest;
import com.woohakdong.api.dto.response.AuthSocialLoginResponse;
import com.woohakdong.api.dto.response.AuthTokensDto;
import com.woohakdong.api.facade.AuthFacade;
import com.woohakdong.exception.CustomAuthException;
import com.woohakdong.framework.security.RequestUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;

import static com.woohakdong.exception.CustomErrorInfo.UNAUTHORIZED_INVALID_TOKEN;

@RestController
@RequestMapping("/api" + "/auth")
@RequiredArgsConstructor
@Tag(name = "Auth", description = "사용자 인증 관련 API")
public class AuthController {

    private final AuthFacade authFacade;

    @Value("${cookie.secure}")
    private boolean cookieSecure;

    @Value("${cookie.same-site}")
    private String cookieSameSite;

    @Operation(summary = "소셜 로그인", description = "소셜 로그인을 통해, JWT 토큰을 발급받습니다.")
    @PostMapping("/social-login")
    public AuthSocialLoginResponse socialLogin(@RequestBody @Valid AuthSocialLoginRequest request,
                                               HttpServletResponse response) {
        AuthTokensDto authTokensDto = authFacade.socialLogin(request);

        // Access Token을 쿠키로 설정 (SSR에서 읽어야 하므로 httpOnly=false)
        ResponseCookie accessTokenCookie = ResponseCookie.from("accessToken", authTokensDto.accessToken())
                .httpOnly(true)
                .secure(cookieSecure)
                .path("/")
                .maxAge(Duration.ofHours(1)) // 1시간
                .sameSite(cookieSameSite)
                .build();

        // Refresh Token을 HttpOnly 쿠키로 설정
        ResponseCookie refreshTokenCookie = ResponseCookie.from("refreshToken", authTokensDto.refreshToken())
                .httpOnly(true)
                .secure(cookieSecure)
                .path("/")
                .maxAge(Duration.ofDays(7)) // 7일
                .sameSite(cookieSameSite)
                .build();

        response.addHeader("Set-Cookie", accessTokenCookie.toString());
        response.addHeader("Set-Cookie", refreshTokenCookie.toString());

        // Response에서는 access token도 반환 (기존 호환성 유지)
        return new AuthSocialLoginResponse(authTokensDto.accessToken());
    }

    @Operation(summary = "Access Token 재발급", description = "Refresh Token을 이용하여 새로운 Access Token을 발급받습니다.")
    @PostMapping("/refresh")
    public AuthSocialLoginResponse refreshAccessToken(HttpServletRequest request,
                                                      HttpServletResponse response) {
        // 쿠키에서 Refresh Token 추출
        Cookie[] cookies = request.getCookies();
        String refreshToken = null;

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("refreshToken".equals(cookie.getName())) {
                    refreshToken = cookie.getValue();
                    break;
                }
            }
        }

        if (refreshToken == null) {
            throw new CustomAuthException(UNAUTHORIZED_INVALID_TOKEN);
        }

        // 새로운 Access Token 발급
        String newAccessToken = authFacade.refreshAccessToken(refreshToken);

        // Access Token을 쿠키로 설정 (SSR에서 읽어야 하므로 httpOnly=false)
        ResponseCookie accessTokenCookie = ResponseCookie.from("accessToken", newAccessToken)
                .httpOnly(false)
                .secure(cookieSecure)
                .path("/")
                .maxAge(Duration.ofHours(1)) // 1시간
                .sameSite(cookieSameSite)
                .build();

        response.addHeader("Set-Cookie", accessTokenCookie.toString());

        // Response에서는 access token도 반환 (기존 호환성 유지)
        return new AuthSocialLoginResponse(newAccessToken);
    }

    @GetMapping("/test")
    public String jwtTest(@AuthenticationPrincipal RequestUser user) {
        return "Login한 유저의 id=" + user.getUserAuthId().toString();
    }
}
