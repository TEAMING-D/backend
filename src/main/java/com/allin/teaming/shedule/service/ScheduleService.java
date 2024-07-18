package com.allin.teaming.shedule.service;

import com.allin.teaming.shedule.domain.Schedule;
import com.allin.teaming.shedule.dto.ScheduleDto.*;
import com.allin.teaming.shedule.repository.ScheduleRepository;
import com.allin.teaming.user.domain.User;
import com.allin.teaming.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ScheduleService {
    private final ScheduleRepository scheduleRepository;
    private final UserRepository userRepository;

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

    // schedule 생성
    @Transactional
    public IdResponse createSchedule(ScheduleCreateDto request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("해당 회원을 찾을 수 없습니다. "));
        Schedule schedule = request.toSchedule(user);
        return IdResponse.of(schedule);
    }

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
