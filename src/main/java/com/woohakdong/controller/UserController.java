package com.woohakdong.controller;

import com.woohakdong.controller.dto.request.UserProfileCreateRequest;
import com.woohakdong.controller.dto.response.UserProfileIdResponse;
import com.woohakdong.controller.dto.response.UserProfileResponse;
import com.woohakdong.facade.UserFacade;
import com.woohakdong.framework.security.RequestUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "UserProfile", description = "사용자 프로필 관련 API")
public class UserController {

    private final UserFacade userFacade;

    @Operation(summary = "내 프로필 조회", description = "인증된 사용자의 프로필 정보를 조회합니다.")
    @GetMapping("/profiles/me")
    public UserProfileResponse getMyProfile(@AuthenticationPrincipal RequestUser user) {
        Long userAuthId = user.getUserAuthId();
        return userFacade.getProfileWithAuthId(userAuthId);
    }

    @Operation(summary = "프로필 정보 입력", description = "소셜 로그인이 완료된 사용자의 프로필 정보를 입력합니다.")
    @PostMapping("/profiles")
    public UserProfileIdResponse createNewProfile(@AuthenticationPrincipal RequestUser user,
                                                  @RequestBody UserProfileCreateRequest request) {
        Long userAuthId = user.getUserAuthId();
        return userFacade.createProfileWithAuthId(userAuthId, request);
    }
}
