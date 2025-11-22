package com.woohakdong.domain.notice.domain;

import com.woohakdong.domain.club.model.ClubEntity;
import com.woohakdong.domain.club.model.ClubMembershipEntity;
import com.woohakdong.domain.notice.infrastructure.storage.NoticeRepository;
import com.woohakdong.domain.notice.model.NoticeCreateCommand;
import com.woohakdong.domain.notice.model.NoticeEntity;
import com.woohakdong.domain.notice.model.NoticeUpdateCommand;
import com.woohakdong.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.woohakdong.exception.CustomErrorInfo.NOT_FOUND_NOTICE;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NoticeDomainService {

    private final NoticeRepository noticeRepository;

    @Transactional
    public Long createNotice(NoticeCreateCommand command, ClubEntity club, ClubMembershipEntity writer) {
        NoticeEntity notice = NoticeEntity.create(command, club, writer);
        NoticeEntity savedNotice = noticeRepository.save(notice);
        return savedNotice.getId();
    }

    public List<NoticeEntity> getNotices(ClubEntity club) {
        return noticeRepository.findAllByClubOrderByIsPinnedDescUpdatedAtDesc(club);
    }

    public NoticeEntity getNotice(Long noticeId, ClubEntity club) {
        return noticeRepository.findByIdAndClub(noticeId, club)
                .orElseThrow(() -> new CustomException(NOT_FOUND_NOTICE));
    }

    @Transactional
    public void updateNotice(NoticeEntity notice, NoticeUpdateCommand command) {
        notice.update(command);
        noticeRepository.save(notice);
    }

    @Transactional
    public void deleteNotice(NoticeEntity notice) {
        noticeRepository.delete(notice);
    }
}
