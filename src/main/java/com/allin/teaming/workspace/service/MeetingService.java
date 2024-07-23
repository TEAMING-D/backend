package com.allin.teaming.workspace.service;

import com.allin.teaming.shedule.domain.Event;
import com.allin.teaming.shedule.domain.Schedule;
import com.allin.teaming.shedule.domain.Week;
import com.allin.teaming.shedule.repository.EventRepository;
import com.allin.teaming.shedule.repository.ScheduleRepository;
import com.allin.teaming.user.domain.MeetingParticipant;
import com.allin.teaming.user.domain.User;
import com.allin.teaming.user.dto.MeetingParticipantDto;
import com.allin.teaming.user.dto.MeetingParticipantDto.MeetingParticipantDeleteDto;
import com.allin.teaming.user.dto.UserDto.*;
import com.allin.teaming.user.repository.MeetingParticipantRepository;
import com.allin.teaming.user.repository.UserRepository;
import com.allin.teaming.workspace.domain.Meeting;
import com.allin.teaming.workspace.domain.Workspace;
import com.allin.teaming.workspace.dto.MeetingDto;
import com.allin.teaming.workspace.dto.MeetingDto.*;
import com.allin.teaming.workspace.repository.MeetingRepository;
import com.allin.teaming.workspace.repository.WorkspaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class MeetingService {

    private final MeetingRepository meetingRepository;
    private final WorkspaceRepository workspaceRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final ScheduleRepository scheduleRepository;
    private final MeetingParticipantRepository meetingParticipantRepository;

    Meeting getMeeting(Long id) {
        return meetingRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 회의를 찾을 수 없습니다. "));
    }

    User getUser(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 회원을 찾을 수 없습니다. "));
    }

    // id로 미팅 조회
    @Transactional(readOnly = true)
    public MeetingDetailDto getMeetingById(Long id) {
        return MeetingDetailDto.of(getMeeting(id));
    }

    // workspace id로 전체 조회
    @Transactional(readOnly = true)
    public List<MeetingDetailDto> getAllMeetingByWorkspaceId(Long id) {
        Workspace workspace = workspaceRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 워크스페이스를 찾을 수 없습니다. "));
        return meetingRepository.findByWorkspace(workspace).stream()
                .map(MeetingDetailDto::of).toList();
    }

    // Title로 조회
    @Transactional
    public MeetingDetailDto getMeetingByTitle(String title) {
        return meetingRepository.findByTitle(title)
                .map(MeetingDetailDto::of)
                .orElseThrow(() -> new IllegalArgumentException("해당 회의를 조회할 수 없습니다. "));
    }

    // userId로 전체 조회
    @Transactional(readOnly = true)
    public List<MeetingDetailDto> getAllMeetingByUserId(Long id) {
        User user = getUser(id);
        return meetingRepository.findByUser(user).stream()
                .map(MeetingDetailDto::of).toList();
    }

    // List<Long> userId 를 사용해서 가능한 회의 시간 전체 조회
    @Transactional(readOnly = true)
    public List<AvailableMeetingTime> getAllAvailableMeetingTime(List<Long> userIds) {
        List<User> users = userIds.stream().map(userRepository::findById)
                .filter(Optional::isPresent).map(Optional::get).toList();
        List<UserScheduleDto> userScheduleDtos = users.stream().map(UserScheduleDto::of).toList();

        List<Schedule> schedules = userIds.stream()
                .map(scheduleRepository::findByUserId)
                .filter(Optional::isPresent)
                .map(Optional::get).toList();

        List<Event> events = schedules.stream()
                .flatMap(schedule -> eventRepository.findByScheduleId(schedule.getId()).stream())
                .toList();

        Map<Week, List<Event>> eventsByWeek = events.stream()
                .collect(Collectors.groupingBy(Event::getWeek, Collectors.collectingAndThen(
                        Collectors.toList(), eventList -> eventList.stream()
                                .sorted(Comparator.comparing(Event::getStart_time))
                                .toList()
                )));


        List<AvailableMeetingTime> availableMeetingTimes = new ArrayList<>();
        Week[] weeks = Week.values();

        for (Week week: weeks) {
            List<Event> eventList = eventsByWeek.getOrDefault(week, Collections.emptyList());
            LocalTime startTime = LocalTime.of(0, 0);

            for (Event event : eventList) {
                if (startTime.isBefore(event.getStart_time())) {
                    availableMeetingTimes.add(AvailableMeetingTime.builder()
                            .start_time(startTime)
                            .end_time(event.getStart_time())
                            .week(week)
                            .userIds(userIds)
                            .userScheduleDtos(userScheduleDtos)
                            .build());
                }
                startTime = event.getEnd_time();
            }
            if (startTime.isBefore(LocalTime.of(23, 59))) {
                availableMeetingTimes.add(AvailableMeetingTime.builder()
                        .start_time(startTime)
                        .end_time(LocalTime.of(23, 59))
                        .week(week)
                        .userIds(userIds)
                        .userScheduleDtos(userScheduleDtos)
                        .build());
            }
        }
        return availableMeetingTimes;
    }

    // 회의 생성
    @Transactional
    public List<MeetingParticipantDto.IdResponse> createMeeting(MeetingCreateDto request) {
        Workspace workspace = workspaceRepository.findById(request.getWorkspaceId())
                .orElseThrow(() -> new IllegalArgumentException("해당 워크스페이스를 조회할 수 없습니다. "));

        if (meetingRepository.existsByTitle(request.getTitle())) {
            throw new RuntimeException("중복된 이름입니다. ");
        }

        Meeting savedMeeting = Meeting.builder()
                .start_time(request.getStart_time())
                .end_time(request.getEnd_time())
                .week(request.getWeek())
                .title(request.getTitle())
                .workspace(workspace)
                .build();

        meetingRepository.save(savedMeeting);

        List<MeetingParticipantDto.IdResponse> ids = new ArrayList<>();

        for (Long userId: request.getUserIds()) {
            User user = getUser(userId);

            MeetingParticipant meetingParticipant = MeetingParticipant.builder()
                    .user(user)
                    .meeting(savedMeeting)
                    .build();

            meetingParticipantRepository.save(meetingParticipant);
            ids.add(MeetingParticipantDto.IdResponse.of(meetingParticipant));
        }
        return ids;
    }

    // 시간 수정
    @Transactional
    public MeetingDto.IdResponse modifyMeetingTime(MeetingTimeModifyDto request) {
        Meeting meeting = getMeeting(request.getId());
        meeting.updateTime(request.getWeek(), request.getStart_time(), request.getEnd_time());
        return MeetingDto.IdResponse.of(meeting);
    }

    // 이름 수정
    @Transactional
    public MeetingDto.IdResponse modifyMeetingTitle(MeetingTitleModifyDto request) {
        Meeting meeting = getMeeting(request.getId());
        meeting.updateTitle(request.getTitle());
        return MeetingDto.IdResponse.of(meeting);
    }

    // 회의 완료
    @Transactional
    public MeetingDto.IdResponse completeMeeting(Long id) {
        return MeetingDto.IdResponse.of(getMeeting(id).complete());
    }

    // 회의 참여자 추가
    @Transactional
    public MeetingParticipantDto.IdResponse addParticipant(Long meetingId, Long userId) {
        Meeting meeting = getMeeting(meetingId);
        User user = getUser(userId);
        if (meetingParticipantRepository.findByMeetingAndUser(meeting,user).isPresent()) {
            throw new IllegalArgumentException("사용자가 이미 미팅에 참여하고 있습니다. ");
        }
        MeetingParticipant meetingParticipant = MeetingParticipant.builder()
                .user(user)
                .meeting(meeting)
                .build();
        meetingParticipantRepository.save(meetingParticipant);
        return MeetingParticipantDto.IdResponse.of(meetingParticipant);
    }

    // 회의 참여자 삭제
    @Transactional
    public MeetingDto.IdResponse deleteParticipants(MeetingParticipantDeleteDto request) {
        final String PARTICIPANT_NOT_FOUND_MESSAGE = "해당 참여원을 찾을 수 없습니다.";

        Meeting meeting = getMeeting(request.getMeetingId());

        List<MeetingParticipant> participantsToDelete = meetingParticipantRepository
                .findAllById(request.getParticipantsId())
                .stream().filter(participants -> request.getMeetingId().equals(participants.getMeeting().getId())).toList();

        // 모든 요청된 참여자가 존재하는지 확인
        if (participantsToDelete.size() != request.getParticipantsId().size()) {
            throw new IllegalArgumentException(PARTICIPANT_NOT_FOUND_MESSAGE);
        }

        // 참여자 삭제
        meetingParticipantRepository.deleteAll(participantsToDelete);

        return MeetingDto.IdResponse.of(meeting);
    }


    // 회의 삭제
    @Transactional
    public void deleteMeeting(Long id) {
        Meeting meeting = getMeeting(id);
        meetingRepository.delete(meeting);
    }
}
