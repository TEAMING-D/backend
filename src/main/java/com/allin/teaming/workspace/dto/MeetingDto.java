package com.allin.teaming.workspace.dto;

import com.allin.teaming.shedule.domain.Week;
import com.allin.teaming.user.domain.MeetingParticipant;
import com.allin.teaming.user.domain.User;
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
        List<UserScheduleDto> meetingParticipantsId = new ArrayList<>();

        public static MeetingDetailDto of(Meeting meeting) {
            return MeetingDetailDto.builder()
                    .id(meeting.getId())
                    .workspaceId(meeting.getWorkspace().getId())
                    .title(meeting.getTitle())
                    .complete(meeting.isComplete())
                    .week(meeting.getWeek())
                    .start_time(meeting.getStart_time())
                    .end_time(meeting.getEnd_time())
                    .build();
        }
    }

    @Getter
    static public class MeetingCreateDto {
        private Long workspaceId;
        private String title;
        private Week week;
        private LocalTime start_time;
        private LocalTime end_time;
        private List<Long> userIds = new ArrayList<>();
        public Meeting toMeeting(Workspace workspace, List<MeetingParticipant> participants) {
            return Meeting.builder()
                    .workspace(workspace)
                    .title(title)
                    .week(week)
                    .start_time(start_time)
                    .end_time(end_time)
                    .complete(false)
                    .meetingParticipants(participants)
                    .build();
        }

    }

    @Builder
    @Getter
    static public class AvailableMeetingTime {
        private LocalTime start_time;
        private LocalTime end_time;
        private Week week;
        private List<Long> userIds;
        private List<UserScheduleDto> userScheduleDtos = new ArrayList<>();
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
    @AllArgsConstructor
    @NoArgsConstructor
    public static class IdResponse{
        private Long id;
        public static IdResponse of(Meeting meeting) {
            return new IdResponse(meeting.getId());
        }
    }
}
