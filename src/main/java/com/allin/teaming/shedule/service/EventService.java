package com.allin.teaming.shedule.service;

import com.allin.teaming.shedule.domain.Event;
import com.allin.teaming.shedule.dto.EventDto.*;
import com.allin.teaming.shedule.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class EventService {
    private final EventRepository eventRepository;

    // 이벤트 id로 조회
    @Transactional(readOnly = true)
    public EventDetailDto getEventById(Long id) {
        return eventRepository.findById(id)
                .map(EventDetailDto::of)
                .orElseThrow(() -> new IllegalArgumentException("해당 이벤트가 존재하지 않습니다. "));
    }

    // event 생성
    @Transactional
    public IdResponse createEvent(EventRegistDto request) {
        if (eventRepository.existsByTitle(request.getTitle())) {
            throw new RuntimeException("이미 존재하는 이벤트 입니다.");
        }
        Event event = request.toEvent();
        eventRepository.save(event);
        return IdResponse.of(event);
    }

    // event 수정
    // TODO : modifyEvent Service 수정
    @Transactional
    public IdResponse modifyEvent() {
        return null;
    }

    // event 삭제
    // TODO : deleteEvent 개발
    @Transactional
    public void deleteEvent() {

    }

}
