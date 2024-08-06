package com.allin.teaming.workspace.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class MeetingAddParticipantDto {
    private Long meetingId;
    private List<Long> userIds;
}
