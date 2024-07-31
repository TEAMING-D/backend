package com.allin.teaming.shedule.service;

import com.allin.teaming.shedule.domain.Event;
import com.allin.teaming.shedule.domain.Schedule;
import com.allin.teaming.shedule.dto.EventDto.*;
import com.allin.teaming.shedule.repository.EventRepository;
import com.allin.teaming.shedule.repository.ScheduleRepository;
import com.allin.teaming.user.Jwt.JwtUtil;
import com.allin.teaming.user.domain.User;
import com.allin.teaming.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EventService {
    private final EventRepository eventRepository;
    private final ScheduleRepository scheduleRepository;
    private final UserRepository userRepository;

    private final JwtUtil jwtUtil;

    private User findUserByToken(String token) {
        return userRepository.findByEmail(jwtUtil.getEmail(token.split(" ")[1]))
            .orElseThrow(() -> new IllegalArgumentException("해당 회원을 조회할 수 없습니다."));
    }

    Schedule findSchedule(Long id) {
       return scheduleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 시간표를 조회할 수 없습니다. "));
    }

    Event findEvent(Long id) {
        return eventRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 이벤트를 조회할 수 없습니다. "));
    }

    // 이벤트 id로 조회
    @Transactional(readOnly = true)
    public EventDetailDto getEventById(Long eventId) {
        return EventDetailDto.of(findEvent(eventId));
    }

    // schedule id로 전체 조회
    @Transactional(readOnly = true)
    public List<EventDetailDto> getAllEventByScheduleId(Long scheduleId) {
        return eventRepository.findByScheduleId(scheduleId).stream()
                .map(EventDetailDto::of).toList();
    }

    // user id로 전체 조회
    @Transactional(readOnly = true)
    public List<EventDetailDto> getAllEventByUser(String token) {
        Long scheduleId = findUserByToken(token).getSchedule().getId();
        return eventRepository.findByScheduleId(scheduleId).stream()
                .map(EventDetailDto::of).toList();


    }

    // event 생성
    @Transactional
    public IdResponse createEvent(String token, EventRegistDto request) {

        Schedule schedule = findUserByToken(token).getSchedule();

        if (eventRepository.existsByScheduleAndTitle(schedule, request.getTitle())) {
            throw new RuntimeException("이미 존재하는 이벤트 입니다.");
        }

        // 겹치는 시간 확인
        if (eventRepository.existsByScheduleAndWeekAndTimeRange(schedule, request.getWeek(), request.getStart_time(), request.getEnd_time())) {
            throw new IllegalArgumentException("일정 시간이 겹칩니다.");
        }

        Event event = request.toEvent(schedule);
        eventRepository.save(event);
        return IdResponse.of(event);
    }

    // event 수정
    // Todo : patch 구현
    // TODO : event 수정 시 회의 도출 어케 할건지
    @Transactional
    public IdResponse modifyEvent(String token, EventModifyDto request) {
        User user = findUserByToken(token);

        Event event = findEvent(request.getEventId());
        Schedule schedule = event.getSchedule();

        if (event.getSchedule() != user.getSchedule()) {
            throw new IllegalArgumentException("해당 일정을 수정할 권한이 없습니다. ");
        }

        if (eventRepository.existsByScheduleAndTitle(schedule, request.getTitle())) {
            throw new IllegalArgumentException("해당 이름을 가진 일정이 존재합니다. ");
        }

        if (eventRepository.existsByScheduleAndWeekAndTimeRange(schedule, request.getWeek(), request.getStart_time(), request.getEnd_time())) {
            throw new RuntimeException("일정 시간이 겹칩니다.");
        }

        event.update(request.getTitle(), request.getInfo(), request.getWeek(),
                request.getStart_time(), request.getEnd_time());
        return IdResponse.of(event);
    }

    // event 삭제
    @Transactional
    public void deleteEvent(String token, Long id) {
        User user = findUserByToken(token);
        Event event = findEvent(id);

        if (event.getSchedule() != user.getSchedule()) {
            throw new IllegalArgumentException("해당 일정을 삭제할 권한이 없습니다. ");
        }
        eventRepository.delete(event);
    }

}
