package com.woohakdong.api.controller;

import com.woohakdong.api.controller.dto.request.AuthSocialLoginRequest;
import com.woohakdong.api.controller.dto.response.AuthSocialLoginResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api" + "/auth")
public class AuthController {

    @PostMapping("/social-login")
    public AuthSocialLoginResponse socialLogin(@RequestBody @Valid AuthSocialLoginRequest request) {
        return null;
    }
}
