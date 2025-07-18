package com.woohakdong.controller;

import com.woohakdong.controller.dto.request.UserProfileCreateRequest;
import com.woohakdong.controller.dto.response.UserProfileIdResponse;
import com.woohakdong.controller.dto.response.UserProfileResponse;
import com.woohakdong.facade.UserFacade;
import com.woohakdong.framework.security.RequestUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api" + "/users")
@RequiredArgsConstructor
public class UserController {

    private final UserFacade userFacade;

    @GetMapping("/profiles/me")
    public UserProfileResponse getMyProfile(@AuthenticationPrincipal RequestUser user) {
        Long userAuthId = user.getUserAuthId();
        return userFacade.getProfileWithAuthId(userAuthId);
    }

    @PostMapping("/profiles")
    public UserProfileIdResponse createNewProfile(@AuthenticationPrincipal RequestUser user,
                                                  @RequestBody UserProfileCreateRequest request) {
        Long userAuthId = user.getUserAuthId();
        return userFacade.createProfileWithAuthId(userAuthId, request);
    }
}
