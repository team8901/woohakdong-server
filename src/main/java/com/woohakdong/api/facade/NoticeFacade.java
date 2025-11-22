package com.woohakdong.api.facade;

import com.woohakdong.api.dto.request.NoticeCreateRequest;
import com.woohakdong.api.dto.request.NoticeUpdateRequest;
import com.woohakdong.api.dto.response.ListWrapper;
import com.woohakdong.api.dto.response.NoticeIdResponse;
import com.woohakdong.api.dto.response.NoticeResponse;
import com.woohakdong.domain.notice.application.NoticeService;
import com.woohakdong.domain.notice.model.NoticeEntity;
import com.woohakdong.domain.user.application.UserService;
import com.woohakdong.domain.user.model.UserProfileEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class NoticeFacade {

    private final NoticeService noticeService;
    private final UserService userService;

    public NoticeIdResponse createNotice(Long clubId, Long userAuthId, NoticeCreateRequest request) {
        UserProfileEntity userProfile = userService.getProfileWithAuthId(userAuthId);
        Long noticeId = noticeService.createNotice(clubId, userProfile, request.toCommand());
        return NoticeIdResponse.of(noticeId);
    }

    public ListWrapper<NoticeResponse> getNotices(Long clubId) {
        List<NoticeEntity> notices = noticeService.getNotices(clubId);
        return ListWrapper.of(notices.stream()
                .map(NoticeResponse::of)
                .toList());
    }

    public NoticeResponse getNotice(Long clubId, Long noticeId) {
        NoticeEntity notice = noticeService.getNotice(clubId, noticeId);
        return NoticeResponse.of(notice);
    }

    public void updateNotice(Long clubId, Long noticeId, Long userAuthId, NoticeUpdateRequest request) {
        UserProfileEntity userProfile = userService.getProfileWithAuthId(userAuthId);
        noticeService.updateNotice(clubId, noticeId, userProfile, request.toCommand());
    }

    public void deleteNotice(Long clubId, Long noticeId, Long userAuthId) {
        UserProfileEntity userProfile = userService.getProfileWithAuthId(userAuthId);
        noticeService.deleteNotice(clubId, noticeId, userProfile);
    }
}
