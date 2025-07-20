package com.woohakdong.controller;

import com.woohakdong.controller.dto.request.ClubRegisterRequest;
import com.woohakdong.controller.dto.response.ClubIdResponse;
import com.woohakdong.facade.ClubFacade;
import com.woohakdong.framework.security.RequestUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api" + "/clubs")
@RequiredArgsConstructor
public class ClubController {

    private final ClubFacade clubFacade;

    @PostMapping
    public ClubIdResponse registerNewClub(@AuthenticationPrincipal RequestUser user, @RequestBody ClubRegisterRequest request) {
        Long userAuthId = user.getUserAuthId();
        return clubFacade.registerNewClub(userAuthId, request);
    }
}
