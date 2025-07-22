package com.woohakdong.controller;

import com.woohakdong.controller.dto.request.ClubNameValidateRequest;
import com.woohakdong.controller.dto.request.ClubRegisterRequest;
import com.woohakdong.controller.dto.response.ClubIdResponse;
import com.woohakdong.controller.dto.response.ClubInfoResponse;
import com.woohakdong.controller.dto.response.ListWrapper;
import com.woohakdong.facade.ClubFacade;
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
@RequestMapping("/api" + "/clubs")
@RequiredArgsConstructor
@Tag(name = "Club", description = "동아리 관련 API")
public class ClubController {

    private final ClubFacade clubFacade;

    @Operation(summary = "동아리 등록", description = "새로운 동아리를 등록합니다.")
    @PostMapping
    public ClubIdResponse registerNewClub(@AuthenticationPrincipal RequestUser user,
                                          @Valid @RequestBody ClubRegisterRequest request) {
        Long userAuthId = user.getUserAuthId();
        return clubFacade.registerNewClub(userAuthId, request);
    }

    @Operation(summary = "내가 가입한 동아리 조회", description = "인증된 사용자가 가입한 동아리 목록을 조회합니다.")
    @GetMapping
    public ListWrapper<ClubInfoResponse> getJoinedClubs(@AuthenticationPrincipal RequestUser user) {
        Long userAuthId = user.getUserAuthId();
        return clubFacade.getJoinedClubs(userAuthId);
    }

    @Operation(summary = "동아리 이름 중복 검사", description = "동아리 이름의 중복 여부를 확인합니다.")
    @PostMapping("/validate-name")
    public void validateClubName(@Valid @RequestBody ClubNameValidateRequest request) {
        clubFacade.validateClubName(request);
    }
}
