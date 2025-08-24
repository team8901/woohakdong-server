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

    @Operation(summary = "소셜 로그인", description = "소셜 로그인을 통해, JWT 토큰을 발급받습니다.")
    @PostMapping("/social-login")
    public AuthSocialLoginResponse socialLogin(@RequestBody @Valid AuthSocialLoginRequest request,
                                               HttpServletResponse response) {
        AuthTokensDto authTokensDto = authFacade.socialLogin(request);

        // Refresh Token을 HttpOnly 쿠키로 설정
        ResponseCookie refreshTokenCookie = ResponseCookie.from("refreshToken", authTokensDto.refreshToken())
                .httpOnly(true)
                .secure(true) // HTTPS에서만 전송
                .path("/")
                .maxAge(Duration.ofDays(7)) // 7일
                .sameSite("Strict") // CSRF 방지
                .build();

        response.addHeader("Set-Cookie", refreshTokenCookie.toString());

        // Response에서는 access token만 반환
        return new AuthSocialLoginResponse(authTokensDto.accessToken());
    }

    @Operation(summary = "Access Token 재발급", description = "Refresh Token을 이용하여 새로운 Access Token을 발급받습니다.")
    @PostMapping("/refresh")
    public AuthSocialLoginResponse refreshAccessToken(HttpServletRequest request) {
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

        // Response에서는 access token만 반환
        return new AuthSocialLoginResponse(newAccessToken);
    }

    @GetMapping("/test")
    public String jwtTest(@AuthenticationPrincipal RequestUser user) {
        return "Login한 유저의 id=" + user.getUserAuthId().toString();
    }
}
