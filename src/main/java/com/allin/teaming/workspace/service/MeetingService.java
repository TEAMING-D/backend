package com.allin.teaming.workspace.service;

import com.allin.teaming.workspace.domain.Meeting;
import com.allin.teaming.workspace.dto.MeetingDTO;
import com.allin.teaming.workspace.exception.MeetingNotFoundException;
import com.allin.teaming.workspace.repository.MeetingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class MeetingService {

    private final MeetingRepository meetingRepository;

    /**
     * 회의 생성
     * @param meetingDTO 생성할 회의의 DTO
     * @return 생성된 회의의 DTO
     */
    public MeetingDTO createMeeting(MeetingDTO meetingDTO) {
        Meeting meeting = new Meeting();
        meeting.setTitle(meetingDTO.getTitle());  // 필드명이 title로 일치해야 함
        meeting.setDescription(meetingDTO.getDescription());
        meeting.setStartTime(meetingDTO.getStartTime());
        meeting.setEndTime(meetingDTO.getEndTime());
        // workspaceId로 Workspace를 찾고 설정하는 로직이 필요할 수 있음
        Meeting savedMeeting = meetingRepository.save(meeting);
        return convertToDTO(savedMeeting);
    }

    /**
     * 회의 ID로 회의 조회
     * @param id 회의 ID
     * @return 조회된 회의의 DTO
     * @throws MeetingNotFoundException 회의가 존재하지 않을 경우 예외 발생
     */
    public MeetingDTO getMeetingById(Long id) {
        Meeting meeting = meetingRepository.findById(id)
                .orElseThrow(() -> new MeetingNotFoundException("Meeting not found with id: " + id));
        return convertToDTO(meeting);
    }

    // Meeting 엔티티를 DTO로 변환하는 메서드
    private MeetingDTO convertToDTO(Meeting meeting) {
        MeetingDTO dto = new MeetingDTO();
        dto.setId(meeting.getId());
        dto.setTitle(meeting.getTitle());
        dto.setDescription(meeting.getDescription());
        dto.setStartTime(meeting.getStartTime());
        dto.setEndTime(meeting.getEndTime());
        dto.setWorkspaceId(meeting.getWorkspace().getId()); // 워크스페이스 ID도 포함시킬 수 있음
        return dto;
    }
}
