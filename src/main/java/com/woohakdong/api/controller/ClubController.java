package com.woohakdong.api.controller;

import com.woohakdong.api.dto.request.ClubApplicationFormCreateRequest;
import com.woohakdong.api.dto.request.ClubNameValidateRequest;
import com.woohakdong.api.dto.request.ClubRegisterRequest;
import com.woohakdong.api.dto.request.ClubUpdateRequest;
import com.woohakdong.api.dto.response.ClubApplicationFormIdResponse;
import com.woohakdong.api.dto.response.ClubApplicationFormInfoResponse;
import com.woohakdong.api.dto.response.ClubIdResponse;
import com.woohakdong.api.dto.response.ClubInfoResponse;
import com.woohakdong.api.dto.response.ListWrapper;
import com.woohakdong.api.facade.ClubFacade;
import com.woohakdong.framework.security.RequestUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

    @Operation(summary = "동아리 정보 수정", description = "동아리 정보를 수정합니다.")
    @PutMapping("/{clubId}")
    public void updateClubInfo(@AuthenticationPrincipal RequestUser user,
                               @PathVariable Long clubId,
                               @Valid @RequestBody ClubUpdateRequest request
    ) {
        Long userAuthId = user.getUserAuthId();
        clubFacade.updateClubInfo(userAuthId, clubId, request);
    }

    @Operation(summary = "동아리 정보 검색", description = "동아리 정보를 검색합니다.")
    @GetMapping("/search")
    public ListWrapper<ClubInfoResponse> searchClubs(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String nameEn
    ) {
        return clubFacade.searchClubs(name, nameEn);
    }

    @Operation(summary = "동아리 신청폼 생성",
            description = "동아리원들이 신청할 수 있는 신청폼을 생성합니다. 이는 동아리 소유자만 생성할 수 있습니다.<p>" +
                    "type은 TEXT, RADIO, CHECKBOX, SELECT 중 하나를 선택할 수 있습니다. <p>" +
                    "required는 필수 입력 여부를 나타내며, true일 경우 해당 질문은 필수로 입력해야 합니다. <p>" +
                    "options는 type이 RADIO, CHECKBOX, SELECT일 때 선택지로 사용됩니다. [JAVA, PYTHON]와 같이 입력 가능")
    @PostMapping("/{clubId}/application-forms")
    public ClubApplicationFormIdResponse createClubApplicationForm(@AuthenticationPrincipal RequestUser user,
                                                                   @PathVariable Long clubId,
                                                                   @Valid @RequestBody ClubApplicationFormCreateRequest request) {
        Long userAuthId = user.getUserAuthId();
        return clubFacade.createClubApplicationForm(clubId, userAuthId, request);
    }

    @Operation(summary = "가장 최신의 동아리 신청폼 조회", description = "동아리 신청폼 중 가장 최신의 것을 조회합니다.")
    @GetMapping("/{clubId}/application-forms/latest")
    public ClubApplicationFormInfoResponse getClubApplicationForm(@PathVariable Long clubId) {
        return clubFacade.getLatestClubApplicationForm(clubId);
    }


}
