package com.allin.teaming.workspace.dto;

import com.allin.teaming.shedule.domain.Week;
import com.allin.teaming.user.domain.MeetingParticipant;
import com.allin.teaming.workspace.domain.Meeting;
import com.allin.teaming.workspace.domain.Workspace;
import lombok.Getter;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Getter
public class MeetingCreateRequestDto {
    private Long workspaceId;
    private String title;
    private Week week;
    private LocalTime start_time;
    private LocalTime end_time;
    private List<Long> userIds = new ArrayList<>();
    public Meeting toMeeting(Workspace workspace) {
        return Meeting.builder()
                .workspace(workspace)
                .title(title)
                .week(week)
                .start_time(start_time)
                .end_time(end_time)
                .complete(false)
                .build();
    }
}
