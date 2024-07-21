package com.allin.teaming.workspace.service;

import com.allin.teaming.shedule.domain.Event;
import com.allin.teaming.shedule.domain.Schedule;
import com.allin.teaming.shedule.domain.Week;
import com.allin.teaming.shedule.repository.EventRepository;
import com.allin.teaming.shedule.repository.ScheduleRepository;
import com.allin.teaming.user.domain.MeetingParticipant;
import com.allin.teaming.user.domain.User;
import com.allin.teaming.user.dto.MeetingParticipantDto;
import com.allin.teaming.user.dto.UserDto.*;
import com.allin.teaming.user.repository.MeetingParticipantRepository;
import com.allin.teaming.user.repository.MeetingRepository;
import com.allin.teaming.user.repository.UserRepository;
import com.allin.teaming.workspace.domain.Meeting;
import com.allin.teaming.workspace.domain.Workspace;
import com.allin.teaming.workspace.dto.MeetingDto;
import com.allin.teaming.workspace.dto.MeetingDto.*;
import com.allin.teaming.workspace.repository.WorkspaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
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

    // userId로 전체 조회
    @Transactional(readOnly = true)
    public List<MeetingDetailDto> getAllMeetingByUserId(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 워크스페이스를 찾을 수 없습니다. "));
        return meetingRepository.findByUser(user).stream()
                .map(MeetingDetailDto::of).toList();
    }

    // List<Long> userId 를 사용해서 가능한 회의 시간 전체 조회
    @Transactional(readOnly = true)
    public List<AvailableMeetingTime> getAllAvailableMeetingTime(List<User> users) {
        List<UserScheduleDto> userScheduleDtos = users.stream().map(UserScheduleDto::of).toList();
        List<Long> userIds = users.stream().map(User::getId).toList();

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
            LocalTime endTime = LocalTime.of(0, 0);

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
    public List<MeetingParticipantDto.IdResponse> createMeeting(AvailableMeetingTime availableMeetingTime, MeetingCreateDto request) {
        Workspace workspace = workspaceRepository.findById(request.getWorkspaceId())
                .orElseThrow(() -> new IllegalArgumentException("해당 워크스페이스를 조회할 수 없습니다. "));

        Meeting savedMeeting = Meeting.builder()
                .start_time(availableMeetingTime.getStart_time())
                .end_time(availableMeetingTime.getEnd_time())
                .week(availableMeetingTime.getWeek())
                .name(request.getName())
                .workspace(workspace)
                .build();

        meetingRepository.save(savedMeeting);

        List<MeetingParticipantDto.IdResponse> ids = new ArrayList<>();

        for (Long userId: availableMeetingTime.getUserIds()) {
            User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("해당 회원을 조회할 수 없습니다. "));

            MeetingParticipant meetingParticipant = MeetingParticipant.builder()
                    .user(user)
                    .meeting(savedMeeting)
                    .build();

            meetingParticipantRepository.save(meetingParticipant);
            ids.add(MeetingParticipantDto.IdResponse.of(meetingParticipant));
        }
        return ids;
    }

    // TODO : 회의 수정
    @Transactional
    public MeetingDto.IdResponse modifyMeeting(Long id) {
        Meeting meeting = getMeeting(id);
        return null;
    }

    // 회의 완료
    @Transactional
    public MeetingDto.IdResponse completeMeeting(Long id) {
        return MeetingDto.IdResponse.of(getMeeting(id).complete());
    }

    // 회의 참여자 추가

    // 회의 참여자 삭제
    @Transactional
    public MeetingDto.IdResponse deleteParticipants(Long meetingId, List<Long> participantsId) {
        final String PARTICIPANT_NOT_FOUND_MESSAGE = "해당 참여원을 찾을 수 없습니다.";

        Meeting meeting = getMeeting(meetingId);

        List<MeetingParticipant> participantsToDelete = meetingParticipantRepository
                .findAllById(participantsId)
                .stream().filter(participants -> meetingId.equals(participants.getMeeting().getId())).toList();

        if (participantsToDelete.size() != participantsId.size()) {
            throw new IllegalArgumentException(PARTICIPANT_NOT_FOUND_MESSAGE);
        }

        meetingParticipantRepository.deleteAll(participantsToDelete);

        return MeetingDto.IdResponse.of(meeting);
    }


    // 회의 삭제
    @Transactional
    public void deleteMeeting(Long id) {
        Meeting meeting = getMeeting(id);
        meetingRepository.delete(meeting);

        List<MeetingParticipant> meetingParticipants = meetingParticipantRepository.findByMeeting(meeting);
        meetingParticipantRepository.deleteAllById(meetingParticipants.stream().map(MeetingParticipant::getId).toList());
    }
}
