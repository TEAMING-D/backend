package com.allin.teaming.workspace.dto;


import com.allin.teaming.shedule.domain.Week;
import com.allin.teaming.user.domain.MeetingParticipant;
import com.allin.teaming.user.dto.UserDto.*;
import com.allin.teaming.workspace.domain.Meeting;
import com.allin.teaming.workspace.domain.Workspace;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class MeetingDto {

    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    static public class MeetingDetailDto {
        private Long id;
        private Long workspaceId;
        private String title;
        private boolean complete;
        private Week week;
        private LocalTime start_time;
        private LocalTime end_time;
        List<UserSimpleDto> users = new ArrayList<>();

        public static MeetingDetailDto of(Meeting meeting) {
            return MeetingDetailDto.builder()
                    .id(meeting.getId())
                    .workspaceId(meeting.getWorkspace().getId())
                    .title(meeting.getTitle())
                    .complete(meeting.isComplete())
                    .week(meeting.getWeek())
                    .start_time(meeting.getStart_time())
                    .end_time(meeting.getEnd_time())
                    .users(meeting.getMeetingParticipants()
                            .stream()
                            .map(MeetingParticipant::getUser)
                            .map(UserSimpleDto::of)
                            .toList())
                    .build();
        }

        public static MeetingDetailDto toDtoWithParticipants(Meeting meeting, List<MeetingParticipant> participants) {
            return MeetingDetailDto.builder()
                    .id(meeting.getId())
                    .workspaceId(meeting.getWorkspace().getId())
                    .title(meeting.getTitle())
                    .complete(meeting.isComplete())
                    .week(meeting.getWeek())
                    .start_time(meeting.getStart_time())
                    .end_time(meeting.getEnd_time())
                    .users(participants.stream()
                            .map(MeetingParticipant::getUser)
                            .map(UserSimpleDto::of)
                            .toList())
                    .build();
        }

    }

    @Getter
    static public class MeetingTitleModifyDto {
        private Long id;
        private String title;
    }

    @Getter
    static public class MeetingTimeModifyDto {
        private Long id;
        private Week week;
        private LocalTime start_time;
        private LocalTime end_time;
    }

    @Getter
    static public class WorkspaceAndUser {
        private Long workspace_id;
        private Long user_id;
    }


    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class IdResponse{
        private Long id;
        public static IdResponse of(Meeting meeting) {
            return new IdResponse(meeting.getId());
        }
    }
}