package com.allin.teaming.workspace.dto;

import com.allin.teaming.shedule.domain.Week;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalTime;
import java.util.List;

@Getter
@Builder
public class MeetingAvailableTimeDto {
    private LocalTime start_time;
    private LocalTime end_time;
    private Week week;
    private List<Long> userIds;
    private Long minusUser;
}
