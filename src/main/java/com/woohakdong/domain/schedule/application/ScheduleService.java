package com.woohakdong.domain.schedule.application;

import com.woohakdong.domain.club.domain.ClubDomainService;
import com.woohakdong.domain.club.model.ClubEntity;
import com.woohakdong.domain.club.model.ClubMemberRole;
import com.woohakdong.domain.club.model.ClubMembershipEntity;
import com.woohakdong.domain.schedule.domain.ScheduleDomainService;
import com.woohakdong.domain.schedule.model.ScheduleCreateCommand;
import com.woohakdong.domain.schedule.model.ScheduleEntity;
import com.woohakdong.domain.schedule.model.ScheduleUpdateCommand;
import com.woohakdong.domain.user.model.UserProfileEntity;
import com.woohakdong.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.woohakdong.exception.CustomErrorInfo.FORBIDDEN_CLUB_OWNER_ONLY;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ScheduleService {

    private final ScheduleDomainService scheduleDomainService;
    private final ClubDomainService clubDomainService;

    @Transactional
    public Long createSchedule(Long clubId, UserProfileEntity userProfile, ScheduleCreateCommand command) {
        ClubEntity club = clubDomainService.getById(clubId);
        ClubMembershipEntity membership = clubDomainService.getClubMemberByUserProfile(clubId, userProfile);
        verifyPresidentRole(membership);
        return scheduleDomainService.createSchedule(command, club, membership);
    }

    public List<ScheduleEntity> getSchedules(Long clubId) {
        ClubEntity club = clubDomainService.getById(clubId);
        return scheduleDomainService.getSchedules(club);
    }

    public ScheduleEntity getSchedule(Long clubId, Long scheduleId) {
        ClubEntity club = clubDomainService.getById(clubId);
        return scheduleDomainService.getSchedule(scheduleId, club);
    }

    @Transactional
    public void updateSchedule(Long clubId, Long scheduleId, UserProfileEntity userProfile, ScheduleUpdateCommand command) {
        ClubEntity club = clubDomainService.getById(clubId);
        ClubMembershipEntity membership = clubDomainService.getClubMemberByUserProfile(clubId, userProfile);
        verifyPresidentRole(membership);
        ScheduleEntity schedule = scheduleDomainService.getSchedule(scheduleId, club);
        scheduleDomainService.updateSchedule(schedule, command);
    }

    @Transactional
    public void deleteSchedule(Long clubId, Long scheduleId, UserProfileEntity userProfile) {
        ClubEntity club = clubDomainService.getById(clubId);
        ClubMembershipEntity membership = clubDomainService.getClubMemberByUserProfile(clubId, userProfile);
        verifyPresidentRole(membership);
        ScheduleEntity schedule = scheduleDomainService.getSchedule(scheduleId, club);
        scheduleDomainService.deleteSchedule(schedule);
    }

    private void verifyPresidentRole(ClubMembershipEntity membership) {
        if (!membership.getClubMemberRole().hasAuthorityOf(ClubMemberRole.PRESIDENT)) {
            throw new CustomException(FORBIDDEN_CLUB_OWNER_ONLY);
        }
    }
}
