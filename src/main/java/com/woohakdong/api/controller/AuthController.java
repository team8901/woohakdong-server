package com.woohakdong.api.controller;

import com.woohakdong.api.controller.dto.request.AuthSocialLoginRequest;
import com.woohakdong.api.controller.dto.response.AuthSocialLoginResponse;
import com.woohakdong.api.facade.AuthFacade;
import com.woohakdong.infrastructure.security.RequestUser;
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
public class AuthController {

    private final AuthFacade authFacade;

    @PostMapping("/social-login")
    public AuthSocialLoginResponse socialLogin(@RequestBody @Valid AuthSocialLoginRequest request) {
        return authFacade.socialLogin(request);
    }

    @GetMapping("/test")
    public String jwtTest(@AuthenticationPrincipal RequestUser user) {
        return "Login한 유저의 id=" + user.getUserAuthId().toString();
    }
}
