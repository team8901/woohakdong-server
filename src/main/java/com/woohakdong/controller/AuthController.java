package com.woohakdong.controller;

import com.woohakdong.controller.dto.request.AuthSocialLoginRequest;
import com.woohakdong.facade.AuthFacade;
import com.woohakdong.controller.dto.response.AuthSocialLoginResponse;
import com.woohakdong.framework.security.RequestUser;
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
