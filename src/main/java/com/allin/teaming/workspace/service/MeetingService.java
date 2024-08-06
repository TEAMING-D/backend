package com.allin.teaming.workspace.service;

import com.allin.teaming.shedule.domain.Event;
import com.allin.teaming.shedule.domain.Schedule;
import com.allin.teaming.shedule.domain.Week;
import com.allin.teaming.shedule.repository.EventRepository;
import com.allin.teaming.shedule.repository.ScheduleRepository;
import com.allin.teaming.user.Jwt.JwtUtil;
import com.allin.teaming.user.domain.MeetingParticipant;
import com.allin.teaming.user.domain.User;
import com.allin.teaming.user.dto.MeetingParticipantDto.MeetingParticipantDeleteDto;
import com.allin.teaming.user.dto.UserDto.*;
import com.allin.teaming.user.repository.MeetingParticipantRepository;
import com.allin.teaming.user.repository.UserRepository;
import com.allin.teaming.workspace.domain.Meeting;
import com.allin.teaming.workspace.domain.Workspace;
import com.allin.teaming.workspace.dto.MeetingAddParticipantDto;
import com.allin.teaming.workspace.dto.MeetingAvailableTimeDto;
import com.allin.teaming.workspace.dto.MeetingCreateRequestDto;
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
    private final JwtUtil jwtUtil;

    Meeting findMeetingByIdMeetingById(Long id) {
        return meetingRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 회의를 찾을 수 없습니다. "));
    }

    User findUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 회원을 찾을 수 없습니다. "));
    }

    User findUserByToken(String token) {
        return userRepository.findByEmail(jwtUtil.getEmail(token.split(" ")[1]))
                .orElseThrow(() -> new IllegalArgumentException("해당 유저를 찾을 수 없습니다."));
    }

    // id로 미팅 조회
    @Transactional(readOnly = true)
    public MeetingDetailDto getMeetingById(Long id) {
        return MeetingDetailDto.of(findMeetingByIdMeetingById(id));
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

    // userId와 Workspace_id로 전체 조회
    @Transactional(readOnly = true)
    public List<MeetingDetailDto> getAllMeetingByUserId(WorkspaceAndUser request) {
        User user = findUserById(request.getUser_id());
        Workspace workspace = workspaceRepository.findById(request.getWorkspace_id())
                .orElseThrow(() -> new IllegalArgumentException("해당 워크 스페이스가 존재하지 않습니다. "));

        List<Meeting> meetings = meetingRepository.findByWorkspace(workspace);
        List<Meeting> userMeeting = new ArrayList<>();
        for (Meeting meeting : meetings) {
            if (meetingParticipantRepository.existsByUserAndMeeting(user, meeting)) {
                userMeeting.add(meeting);
            }
        }

        return userMeeting.stream().map(MeetingDetailDto::of).toList();
    }

    // workspace 내의 나의 모든 회의 조회
    public List<MeetingDetailDto> getMyMeetingByWorkspaceId(String token, Long workspaceId) {
        Workspace workspace = workspaceRepository.findById(workspaceId)
                .orElseThrow(() -> new IllegalArgumentException("해당 워크 스페이스를 조회할 수 없습니다."));
        User user = findUserByToken(token);

        // workspace의 모든 미팅 조회
        List<Meeting> meetings = meetingRepository.findByWorkspace(workspace);

        // 모든 미팅 중 내가 참여한 미팅만 거르기
        List<Meeting> myMeetings = new ArrayList<>();
        for (Meeting meeting : meetings) {
            if (meetingParticipantRepository.existsByUserAndMeeting(user, meeting)) {
                myMeetings.add(meeting);
            }
        }
        return myMeetings.stream().map(MeetingDetailDto::of).toList();
    }

    // TODO : 모든 조합으로 다 도출하기
//    public List<List<MeetingAvailableTimeDto>> getAllAvailableMeetingTimeForAllCombination(List<Long> userIds) {
//        List<User> users = userIds.stream().map(userRepository::findById)
//                .filter(Optional::isPresent).map(Optional::get).toList();
//
//        List<User> selectedUsers = new ArrayList<>();
//        // 한명 제외하기
//        for (User x : users) {
//            selectedUsers = users.stream()
//                    .filter((user) -> !user.equals(x))
//                    .toList();
//            List<Long> getSelectedMembersId = selectedUsers.stream().map(User::getId).toList();
//            List<MeetingAvailableTimeDto> getAllAvailableMeetingTime(getSelectedMembersId);
//
//        }
//    }

    // List<Long> userId 를 사용해서 가능한 회의 시간 전체 조회
    // TODO : 수정 예정!
    @Transactional(readOnly = true)
    public List<MeetingAvailableTimeDto> getAllAvailableMeetingTime(List<Long> userIds) {
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


        List<MeetingAvailableTimeDto> availableMeetingTimes = new ArrayList<>();
        Week[] weeks = Week.values();

        // 전체 가능한 시간 도출
        for (Week week: weeks) {
            List<Event> eventList = eventsByWeek.getOrDefault(week, Collections.emptyList());
            LocalTime startTime = LocalTime.of(0, 0);

            for (Event event : eventList) {
                if (startTime.isBefore(event.getStart_time())) {
                    availableMeetingTimes.add(MeetingAvailableTimeDto.builder()
                            .start_time(startTime)
                            .end_time(event.getStart_time())
                            .week(week)
                            .userIds(userIds)
                            .build());
                }
                startTime = event.getEnd_time();
            }
            if (startTime.isBefore(LocalTime.of(23, 59))) {
                availableMeetingTimes.add(MeetingAvailableTimeDto.builder()
                        .start_time(startTime)
                        .end_time(LocalTime.of(23, 59))
                        .week(week)
                        .userIds(userIds)
                        .build());
            }
        }
        return availableMeetingTimes;
    }


    // 회의 생성
    @Transactional
    public MeetingDetailDto createMeeting(MeetingCreateRequestDto request) {
        Workspace workspace = workspaceRepository.findById(request.getWorkspaceId())
                .orElseThrow(() -> new IllegalArgumentException("해당 워크스페이스를 조회할 수 없습니다. "));

        if (meetingRepository.existsByTitle(request.getTitle())) {
            throw new RuntimeException("중복된 이름입니다. ");
        }

        Meeting savedMeeting = request.toMeeting(workspace);

        meetingRepository.save(savedMeeting);

        List<MeetingParticipant> initParticipants = new ArrayList<>();
        for (Long userId: request.getUserIds()) {
            User user = findUserById(userId);

            initParticipants.add(MeetingParticipant.builder()
                    .user(user)
                    .meeting(savedMeeting)
                    .build());
        }

        meetingParticipantRepository.saveAll(initParticipants);
        System.out.println("여기까진 되나??");
        return MeetingDetailDto.toDtoWithParticipants(savedMeeting, initParticipants);
    }

    // 회의 시간 수정
    @Transactional
    public MeetingDetailDto modifyMeetingTime(MeetingTimeModifyDto request) {
        Meeting meeting = findMeetingByIdMeetingById(request.getId());
        meeting.updateTime(request.getWeek(), request.getStart_time(), request.getEnd_time());
        return MeetingDetailDto.of(meeting);
    }

    // 이름 수정
    @Transactional
    public MeetingDetailDto modifyMeetingTitle(MeetingTitleModifyDto request) {
        Meeting meeting = findMeetingByIdMeetingById(request.getId());
        meeting.updateTitle(request.getTitle());
        return MeetingDetailDto.of(meeting);
    }

    // 회의 완료
    @Transactional
    public MeetingDetailDto completeMeeting(Long id) {
        return MeetingDetailDto.of(findMeetingByIdMeetingById(id).complete());
    }

    // 회의 참여자 추가
    @Transactional
    public MeetingDetailDto addParticipant(MeetingAddParticipantDto request) {
        Meeting meeting = findMeetingByIdMeetingById(request.getMeetingId());
        List<User> users = request.getUserIds().stream().map(this::findUserById).toList();

        List<MeetingParticipant> addParticipants = new ArrayList<>();
        for (User user : users) {
            if (meetingParticipantRepository.findByMeetingAndUser(meeting,user).isPresent()) {
                throw new IllegalArgumentException("사용자가 이미 미팅에 참여하고 있습니다. ");
            }
            addParticipants.add(MeetingParticipant.builder()
                    .user(user)
                    .meeting(meeting)
                    .build());
        }

        meetingParticipantRepository.saveAll(addParticipants);

        return MeetingDetailDto.of(meeting);
    }

    // 회의 참여자 삭제
    @Transactional
    public MeetingDetailDto deleteParticipants(MeetingParticipantDeleteDto request) {
        final String PARTICIPANT_NOT_FOUND_MESSAGE = "해당 참여원을 찾을 수 없습니다. userId = ";

        Meeting meeting = findMeetingByIdMeetingById(request.getMeetingId());

        /*
        삭제해달라고 요청받은 모든 멤버들을 찾고
         */
        List<MeetingParticipant> participantsToDelete = new ArrayList<>();
        for (Long userId : request.getUserIds()){
            participantsToDelete.add(meetingParticipantRepository.findByMeetingAndUser(meeting, findUserById(userId))
                    .orElseThrow(() -> new IllegalArgumentException(PARTICIPANT_NOT_FOUND_MESSAGE + userId)));
        }

        // 참여자 삭제
        meetingParticipantRepository.deleteAll(participantsToDelete);

        return MeetingDetailDto.of(meeting);
    }


    // 회의 삭제
    @Transactional
    public void deleteMeeting(Long id) {
        Meeting meeting = findMeetingByIdMeetingById(id);
        meetingRepository.delete(meeting);
    }
}