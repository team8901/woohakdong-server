package com.woohakdong.api.controller;

import com.woohakdong.api.dto.request.NoticeCreateRequest;
import com.woohakdong.api.dto.request.NoticeUpdateRequest;
import com.woohakdong.api.dto.response.ListWrapper;
import com.woohakdong.api.dto.response.NoticeIdResponse;
import com.woohakdong.api.dto.response.NoticeResponse;
import com.woohakdong.api.facade.NoticeFacade;
import com.woohakdong.framework.security.RequestUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/clubs/{clubId}/notices")
@RequiredArgsConstructor
@Tag(name = "Notice", description = "공지사항 관련 API")
public class NoticeController {

    private final NoticeFacade noticeFacade;

    @Operation(summary = "공지사항 작성", description = "동아리 공지사항을 작성합니다.")
    @PostMapping
    public NoticeIdResponse createNotice(@AuthenticationPrincipal RequestUser user,
                                         @PathVariable Long clubId,
                                         @Valid @RequestBody NoticeCreateRequest request) {
        Long userAuthId = user.getUserAuthId();
        return noticeFacade.createNotice(clubId, userAuthId, request);
    }

    @Operation(summary = "공지사항 목록 조회", description = "동아리 공지사항 목록을 조회합니다. 고정된 공지가 먼저 표시됩니다.")
    @GetMapping
    public ListWrapper<NoticeResponse> getNotices(@PathVariable Long clubId) {
        return noticeFacade.getNotices(clubId);
    }

    @Operation(summary = "공지사항 단건 조회", description = "동아리 공지사항을 조회합니다.")
    @GetMapping("/{noticeId}")
    public NoticeResponse getNotice(@PathVariable Long clubId,
                                    @PathVariable Long noticeId) {
        return noticeFacade.getNotice(clubId, noticeId);
    }

    @Operation(summary = "공지사항 수정", description = "동아리 공지사항을 수정합니다.")
    @PutMapping("/{noticeId}")
    public void updateNotice(@AuthenticationPrincipal RequestUser user,
                             @PathVariable Long clubId,
                             @PathVariable Long noticeId,
                             @Valid @RequestBody NoticeUpdateRequest request) {
        Long userAuthId = user.getUserAuthId();
        noticeFacade.updateNotice(clubId, noticeId, userAuthId, request);
    }

    @Operation(summary = "공지사항 삭제", description = "동아리 공지사항을 삭제합니다.")
    @DeleteMapping("/{noticeId}")
    public void deleteNotice(@AuthenticationPrincipal RequestUser user,
                             @PathVariable Long clubId,
                             @PathVariable Long noticeId) {
        Long userAuthId = user.getUserAuthId();
        noticeFacade.deleteNotice(clubId, noticeId, userAuthId);
    }
}
