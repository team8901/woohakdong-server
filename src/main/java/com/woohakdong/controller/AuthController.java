package com.woohakdong.controller;

import com.woohakdong.controller.dto.request.AuthSocialLoginRequest;
import com.woohakdong.facade.AuthFacade;
import com.woohakdong.controller.dto.response.AuthSocialLoginResponse;
import com.woohakdong.framework.security.RequestUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api" + "/auth")
@RequiredArgsConstructor
@Tag(name = "Auth", description = "사용자 인증 관련 API")
public class AuthController {

    private final AuthFacade authFacade;

    @Operation(summary = "소셜 로그인", description = "소셜 로그인을 통해, JWT 토큰을 발급받습니다.")
    @PostMapping("/social-login")
    public AuthSocialLoginResponse socialLogin(@RequestBody @Valid AuthSocialLoginRequest request) {
        return authFacade.socialLogin(request);
    }

    @GetMapping("/test")
    public String jwtTest(@AuthenticationPrincipal RequestUser user) {
        return "Login한 유저의 id=" + user.getUserAuthId().toString();
    }
}
