package com.woohakdong.domain.notice.application;

import com.woohakdong.domain.club.domain.ClubDomainService;
import com.woohakdong.domain.club.model.ClubEntity;
import com.woohakdong.domain.club.model.ClubMembershipEntity;
import com.woohakdong.domain.notice.domain.NoticeDomainService;
import com.woohakdong.domain.notice.model.NoticeCreateCommand;
import com.woohakdong.domain.notice.model.NoticeEntity;
import com.woohakdong.domain.notice.model.NoticeUpdateCommand;
import com.woohakdong.domain.user.model.UserProfileEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NoticeService {

    private final NoticeDomainService noticeDomainService;
    private final ClubDomainService clubDomainService;

    @Transactional
    public Long createNotice(Long clubId, UserProfileEntity userProfile, NoticeCreateCommand command) {
        ClubEntity club = clubDomainService.getById(clubId);
        ClubMembershipEntity membership = clubDomainService.getClubMemberByUserProfile(clubId, userProfile);
        // TODO: 권한 체크 (임원 이상만 작성 가능 등)
        return noticeDomainService.createNotice(command, club, membership);
    }

    public List<NoticeEntity> getNotices(Long clubId) {
        ClubEntity club = clubDomainService.getById(clubId);
        return noticeDomainService.getNotices(club);
    }

    public NoticeEntity getNotice(Long clubId, Long noticeId) {
        ClubEntity club = clubDomainService.getById(clubId);
        return noticeDomainService.getNotice(noticeId, club);
    }

    @Transactional
    public void updateNotice(Long clubId, Long noticeId, UserProfileEntity userProfile, NoticeUpdateCommand command) {
        ClubEntity club = clubDomainService.getById(clubId);
        NoticeEntity notice = noticeDomainService.getNotice(noticeId, club);
        // TODO: 권한 체크 (작성자 또는 임원 이상만 수정 가능 등)
        noticeDomainService.updateNotice(notice, command);
    }

    @Transactional
    public void deleteNotice(Long clubId, Long noticeId, UserProfileEntity userProfile) {
        ClubEntity club = clubDomainService.getById(clubId);
        NoticeEntity notice = noticeDomainService.getNotice(noticeId, club);
        // TODO: 권한 체크 (작성자 또는 임원 이상만 삭제 가능 등)
        noticeDomainService.deleteNotice(notice);
    }
}
