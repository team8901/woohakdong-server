package com.woohakdong.api.facade;

import com.woohakdong.api.dto.request.ActivityCreateRequest;
import com.woohakdong.api.dto.request.ActivityUpdateRequest;
import com.woohakdong.api.dto.response.ActivityIdResponse;
import com.woohakdong.api.dto.response.ActivityResponse;
import com.woohakdong.api.dto.response.ListWrapper;
import com.woohakdong.domain.activity.application.ActivityService;
import com.woohakdong.domain.activity.model.ActivityEntity;
import com.woohakdong.domain.user.application.UserService;
import com.woohakdong.domain.user.model.UserProfileEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ActivityFacade {

    private final ActivityService activityService;
    private final UserService userService;

    public ActivityIdResponse createActivity(Long clubId, Long userAuthId, ActivityCreateRequest request) {
        UserProfileEntity userProfile = userService.getProfileWithAuthId(userAuthId);
        Long activityId = activityService.createActivity(clubId, userProfile, request.toCommand());
        return ActivityIdResponse.of(activityId);
    }

    public ListWrapper<ActivityResponse> getActivities(Long clubId) {
        List<ActivityEntity> activities = activityService.getActivities(clubId);
        return ListWrapper.of(activities.stream()
                .map(ActivityResponse::of)
                .toList());
    }

    public ActivityResponse getActivity(Long clubId, Long activityId) {
        ActivityEntity activity = activityService.getActivity(clubId, activityId);
        return ActivityResponse.of(activity);
    }

    public void updateActivity(Long clubId, Long activityId, Long userAuthId, ActivityUpdateRequest request) {
        UserProfileEntity userProfile = userService.getProfileWithAuthId(userAuthId);
        activityService.updateActivity(clubId, activityId, userProfile, request.toCommand());
    }

    public void deleteActivity(Long clubId, Long activityId, Long userAuthId) {
        UserProfileEntity userProfile = userService.getProfileWithAuthId(userAuthId);
        activityService.deleteActivity(clubId, activityId, userProfile);
    }
}
