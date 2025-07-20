package com.woohakdong.controller;

import com.woohakdong.controller.dto.request.ClubRegisterRequest;
import com.woohakdong.controller.dto.response.ClubIdResponse;
import com.woohakdong.facade.ClubFacade;
import com.woohakdong.framework.security.RequestUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api" + "/clubs")
@RequiredArgsConstructor
@Tag(name = "Club", description = "동아리 관련 API")
public class ClubController {

    private final ClubFacade clubFacade;

    @Operation(summary = "동아리 등록", description = "새로운 동아리를 등록합니다.")
    @PostMapping
    public ClubIdResponse registerNewClub(@AuthenticationPrincipal RequestUser user, @RequestBody ClubRegisterRequest request) {
        Long userAuthId = user.getUserAuthId();
        return clubFacade.registerNewClub(userAuthId, request);
    }
}
