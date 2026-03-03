package com.woohakdong.domain.schedule.domain;

import com.woohakdong.domain.club.model.ClubEntity;
import com.woohakdong.domain.club.model.ClubMembershipEntity;
import com.woohakdong.domain.schedule.infrastructure.storage.ScheduleRepository;
import com.woohakdong.domain.schedule.model.ScheduleCreateCommand;
import com.woohakdong.domain.schedule.model.ScheduleEntity;
import com.woohakdong.domain.schedule.model.ScheduleUpdateCommand;
import com.woohakdong.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.woohakdong.exception.CustomErrorInfo.NOT_FOUND_SCHEDULE;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ScheduleDomainService {

    private final ScheduleRepository scheduleRepository;

    @Transactional
    public Long createSchedule(ScheduleCreateCommand command, ClubEntity club, ClubMembershipEntity writer) {
        ScheduleEntity schedule = ScheduleEntity.create(command, club, writer);
        ScheduleEntity savedSchedule = scheduleRepository.save(schedule);
        return savedSchedule.getId();
    }

    public List<ScheduleEntity> getSchedules(ClubEntity club) {
        return scheduleRepository.findAllByClubAndDeletedAtIsNullOrderByStartTimeAsc(club);
    }

    public ScheduleEntity getSchedule(Long scheduleId, ClubEntity club) {
        return scheduleRepository.findByIdAndClubAndDeletedAtIsNull(scheduleId, club)
                .orElseThrow(() -> new CustomException(NOT_FOUND_SCHEDULE));
    }

    @Transactional
    public void updateSchedule(ScheduleEntity schedule, ScheduleUpdateCommand command) {
        schedule.update(command);
        scheduleRepository.save(schedule);
    }

    @Transactional
    public void deleteSchedule(ScheduleEntity schedule) {
        schedule.delete();
        scheduleRepository.save(schedule);
    }
}
