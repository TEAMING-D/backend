package com.allin.teaming.shedule.service;

import com.allin.teaming.shedule.domain.Event;
import com.allin.teaming.shedule.domain.Schedule;
import com.allin.teaming.shedule.dto.EventDto;
import com.allin.teaming.shedule.dto.ScheduleDto.*;
import com.allin.teaming.shedule.repository.EventRepository;
import com.allin.teaming.shedule.repository.ScheduleRepository;
import com.allin.teaming.user.domain.User;
import com.allin.teaming.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ScheduleService {
    private final ScheduleRepository scheduleRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;

    // id로 조회
    @Transactional(readOnly = true)
    public ScheduleDetailDto getScheduleById(Long id) {
        return scheduleRepository.findById(id)
                .map(ScheduleDetailDto::of).orElseThrow(() -> new IllegalArgumentException("해당 스케줄을 찾을 수 없습니다. "));
    }

    // userId로 조회
    @Transactional(readOnly = true)
    public ScheduleDetailDto getScheduleByUserId(Long id) {
        return scheduleRepository.findByUserId(id)
                .map(ScheduleDetailDto::of).orElseThrow(() -> new IllegalArgumentException("해당 스케줄을 찾을 수 없습니다. "));
    }

    // 빈 스케줄 생성
//    @Transactional
//    IdResponse createSchedule(ScheduleCreateDto request) {
//        User user = userRepository.findById(request.getUserId())
//                .orElseThrow(() -> new IllegalArgumentException("해당 회원을 조회할 수 없습니다. "));
//
//        Schedule
//    }

    // everytime 링크 연결



    // schedule 생성
//    @Transactional
//    public IdResponse initSchedule(ScheduleCreateDto request) {
//        User user = userRepository.findById(request.getUserId())
//                .orElseThrow(() -> new IllegalArgumentException("해당 회원을 찾을 수 없습니다. "));
//        Schedule schedule = request.toSchedule(user);
//        scheduleRepository.save(schedule);
//        List<Event> events = request.getEvents().stream().map((eventDto) -> eventDto.toEvent(schedule)).toList();
//        events.forEach(eventRepository::save);
//        return IdResponse.of(schedule);
//    }

    // schedule 수정
    @Transactional
    public IdResponse modifySchedule(Long id, ScheduleModifyDto request) {
        Schedule schedule = scheduleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 스케줄을 찾을 수 없습니다. "));
        schedule.update(request.getTitle());
        return IdResponse.of(schedule);
    }

    // schedule 삭제
    @Transactional
    public void deleteSchedule(Long id) {
        Schedule schedule = scheduleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 스케줄을 찾을 수 없습니다. "));
        scheduleRepository.delete(schedule);
    }
}
