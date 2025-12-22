package com.woohakdong.domain.activity.domain;

import com.woohakdong.domain.activity.infrastructure.storage.ActivityRepository;
import com.woohakdong.domain.activity.model.ActivityCreateCommand;
import com.woohakdong.domain.activity.model.ActivityEntity;
import com.woohakdong.domain.activity.model.ActivityUpdateCommand;
import com.woohakdong.domain.club.model.ClubEntity;
import com.woohakdong.domain.club.model.ClubMembershipEntity;
import com.woohakdong.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.woohakdong.exception.CustomErrorInfo.NOT_FOUND_ACTIVITY;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ActivityDomainService {

    private final ActivityRepository activityRepository;

    @Transactional
    public Long createActivity(ActivityCreateCommand command, ClubEntity club, ClubMembershipEntity writer) {
        ActivityEntity activity = ActivityEntity.create(command, club, writer);
        ActivityEntity savedActivity = activityRepository.save(activity);
        return savedActivity.getId();
    }

    public List<ActivityEntity> getActivities(ClubEntity club) {
        return activityRepository.findAllByClubAndDeletedAtIsNullOrderByActivityDateDescCreatedAtDesc(club);
    }

    public ActivityEntity getActivity(Long activityId, ClubEntity club) {
        return activityRepository.findByIdAndClubAndDeletedAtIsNull(activityId, club)
                .orElseThrow(() -> new CustomException(NOT_FOUND_ACTIVITY));
    }

    @Transactional
    public void updateActivity(ActivityEntity activity, ActivityUpdateCommand command) {
        activity.update(command);
        activityRepository.save(activity);
    }

    @Transactional
    public void deleteActivity(ActivityEntity activity) {
        activity.delete();
        activityRepository.save(activity);
    }
}
