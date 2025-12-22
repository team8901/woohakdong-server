package com.woohakdong.domain.activity.application;

import com.woohakdong.domain.activity.domain.ActivityDomainService;
import com.woohakdong.domain.activity.model.ActivityCreateCommand;
import com.woohakdong.domain.activity.model.ActivityEntity;
import com.woohakdong.domain.activity.model.ActivityUpdateCommand;
import com.woohakdong.domain.club.domain.ClubDomainService;
import com.woohakdong.domain.club.model.ClubEntity;
import com.woohakdong.domain.club.model.ClubMembershipEntity;
import com.woohakdong.domain.user.model.UserProfileEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ActivityService {

    private final ActivityDomainService activityDomainService;
    private final ClubDomainService clubDomainService;

    @Transactional
    public Long createActivity(Long clubId, UserProfileEntity userProfile, ActivityCreateCommand command) {
        ClubEntity club = clubDomainService.getById(clubId);
        ClubMembershipEntity membership = clubDomainService.getClubMemberByUserProfile(clubId, userProfile);
        // TODO: 권한 체크 (임원 이상만 작성 가능 등)
        return activityDomainService.createActivity(command, club, membership);
    }

    public List<ActivityEntity> getActivities(Long clubId) {
        ClubEntity club = clubDomainService.getById(clubId);
        return activityDomainService.getActivities(club);
    }

    public ActivityEntity getActivity(Long clubId, Long activityId) {
        ClubEntity club = clubDomainService.getById(clubId);
        return activityDomainService.getActivity(activityId, club);
    }

    @Transactional
    public void updateActivity(Long clubId, Long activityId, UserProfileEntity userProfile, ActivityUpdateCommand command) {
        ClubEntity club = clubDomainService.getById(clubId);
        ActivityEntity activity = activityDomainService.getActivity(activityId, club);
        // TODO: 권한 체크 (작성자 또는 임원 이상만 수정 가능 등)
        activityDomainService.updateActivity(activity, command);
    }

    @Transactional
    public void deleteActivity(Long clubId, Long activityId, UserProfileEntity userProfile) {
        ClubEntity club = clubDomainService.getById(clubId);
        ActivityEntity activity = activityDomainService.getActivity(activityId, club);
        // TODO: 권한 체크 (작성자 또는 임원 이상만 삭제 가능 등)
        activityDomainService.deleteActivity(activity);
    }
}
