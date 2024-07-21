package com.allin.teaming.user.dto;


import com.allin.teaming.user.domain.MeetingParticipant;
import com.allin.teaming.user.domain.User;
import com.allin.teaming.workspace.domain.Meeting;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class MeetingParticipantDto {
    private Long userId;
    private Long meetingId;

    public MeetingParticipant toMeetingParticipant(User user, Meeting meeting) {
        return MeetingParticipant.builder()
                .user(user)
                .meeting(meeting)
                .build();
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class IdResponse{
        private Long id;
        public static IdResponse of(MeetingParticipant meetingParticipant) {
            return new IdResponse(meetingParticipant.getId());
        }
    }
}
