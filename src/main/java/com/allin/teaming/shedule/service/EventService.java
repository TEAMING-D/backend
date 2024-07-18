package com.allin.teaming.shedule.service;

import com.allin.teaming.shedule.domain.Event;
import com.allin.teaming.shedule.domain.Schedule;
import com.allin.teaming.shedule.dto.EventDto.*;
import com.allin.teaming.shedule.repository.EventRepository;
import com.allin.teaming.shedule.repository.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EventService {
    private final EventRepository eventRepository;
    private final ScheduleRepository scheduleRepository;

    // 이벤트 id로 조회
    @Transactional(readOnly = true)
    public EventDetailDto getEventById(Long id) {
        return eventRepository.findById(id)
                .map(EventDetailDto::of)
                .orElseThrow(() -> new IllegalArgumentException("해당 이벤트가 존재하지 않습니다. "));
    }

    // schedule id로 전체 조회
    @Transactional(readOnly = true)
    public List<EventDetailDto> getAllEventByScheduleId(Long id) {
        return eventRepository.findByScheduleId(id).stream()
                .map(EventDetailDto::of).toList();
    }

    // event 생성
    @Transactional
    public IdResponse createEvent(EventRegistDto request) {
        if (eventRepository.existsByTitle(request.getTitle())) {
            throw new RuntimeException("이미 존재하는 이벤트 입니다.");
        }

        Schedule schedule = scheduleRepository.findById(request.getScheduleId())
                .orElseThrow(() -> new IllegalArgumentException("해당 스케줄이 존재하지 않습니다. "));

        Event event = request.toEvent(schedule);
        eventRepository.save(event);
        return IdResponse.of(event);
    }

    // event 수정
    @Transactional
    public IdResponse modifyEvent(Long id, EventModifyDto request) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 이벤트가 존재하지 않습니다. "));
        event.update(request.getTitle(), request.getInfo(), request.getWeek(),
                request.getStart_time(), request.getEnd_time());
        return IdResponse.of(event);
    }

    // event 삭제
    @Transactional
    public void deleteEvent(Long id) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 이벤트를 찾을 수 없습니다. "));
        eventRepository.delete(event);
    }

}
