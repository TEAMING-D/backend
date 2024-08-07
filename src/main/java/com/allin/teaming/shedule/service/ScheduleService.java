package com.allin.teaming.shedule.service;

import com.allin.teaming.shedule.domain.Schedule;
import com.allin.teaming.shedule.dto.ScheduleDto.*;
import com.allin.teaming.shedule.repository.EventRepository;
import com.allin.teaming.shedule.repository.ScheduleRepository;
import com.allin.teaming.user.Jwt.JwtUtil;
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
    private final EventRepository eventRepository;

    private final JwtUtil jwtUtil;

    private User findUserByToken(String token) {
        return userRepository.findByEmail(jwtUtil.getEmail(token.split(" ")[1]))
            .orElseThrow(() -> new IllegalArgumentException("해당 회원을 조회할 수 없습니다."));
    }

    private Schedule findScheduleById(Long scheduleId) {
        return scheduleRepository.findById(scheduleId)
            .orElseThrow(() -> new IllegalArgumentException("해당 학교를 조회할 수 없습니다. "));
    }

    // id로 조회
    @Transactional(readOnly = true)
    public ScheduleDetailDto getScheduleById(Long scheduleId) {
        return ScheduleDetailDto.of(findScheduleById(scheduleId));
    }

    // 내 시간표 조회
    @Transactional(readOnly = true)
    public ScheduleDetailDto getScheduleByUser(String token) {
        User user = findUserByToken(token);
        if (user.getSchedule() != null) {
            return ScheduleDetailDto.of(findUserByToken(token).getSchedule());
        } else throw new IllegalArgumentException("아직 시간표를 생성하지 않았습니다. ");
    }


    // UserId로 스케줄 조회
    @Transactional(readOnly = true)
    public ScheduleDetailDto getScheduleByUserId(Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("해당 유저를 찾을 수 없습니다. "));
        if (user.getSchedule() != null) {
            return ScheduleDetailDto.of(user.getSchedule());
        }else throw new IllegalArgumentException("아직 시간표를 생성하지 않았습니다. ");
    }

    // 초기 스케줄 생성
    @Transactional
    public IdResponse createSchedule(String token, ScheduleCreateDto request) {
        User user = findUserByToken(token);
        Schedule schedule = request.toEntity(user);
        scheduleRepository.save(schedule);

        if (!request.getEvents().isEmpty()) {
            request.getEvents().forEach((event) -> {
                eventRepository.save(event.toEvent(schedule));
            });
        }
        return IdResponse.of(schedule);
    }

    // schedule 수정
    @Transactional
    public IdResponse modifySchedule(String token, ScheduleModifyDto request) {
        User user = findUserByToken(token);
//        Schedule schedule = findScheduleById(request.getScheduleId());
        Schedule schedule = scheduleRepository.findByUserId(user.getId())
                .orElseThrow(() -> new IllegalArgumentException("해당 유저의 스케줄을 찾을 수 없습니다. "));
        schedule.update(request.getTitle());
        return IdResponse.of(schedule);
    }



    // schedule 삭제
    @Transactional
    public void deleteSchedule(Long id) {
        scheduleRepository.delete(findScheduleById(id));
    }
}
