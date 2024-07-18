package com.allin.teaming.shedule.controller;

import com.allin.teaming.Response.BasicResponse;
import com.allin.teaming.Response.DataResponse;
import com.allin.teaming.shedule.dto.EventDto.*;
import com.allin.teaming.shedule.service.EventService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/events")
public class EventController {

    private final EventService eventService;

    // Todo : postman test
    // event id로 조회
    @GetMapping("/{event_id}")
    public ResponseEntity<? extends BasicResponse> getEventByID(
            @PathVariable("event_id") Long id) {
        return ResponseEntity.ok(
                new DataResponse<>(eventService.getEventById(id)));
    }

    // schedule id로 전체 조회
    @GetMapping("{schedule_id}")
    public ResponseEntity<? extends BasicResponse> getAllEventByScheduleID(
            @PathVariable("schedule_id") Long id) {
        return ResponseEntity.ok(new DataResponse<>(eventService.getAllEventByScheduleId(id)));

    }

    // event 생성
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<? extends BasicResponse> createEvent(
            @Valid @RequestBody EventRegistDto request) {
        return ResponseEntity.ok().body(
                new DataResponse<>(eventService.createEvent(request)));
    }

    // event 수정
    @PatchMapping(value = "/{event_id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<? extends BasicResponse> modifyEvent(
            @PathVariable("event_id") Long id,
            @RequestBody EventModifyDto request) {
        return ResponseEntity.ok(new DataResponse<>(eventService.modifyEvent(id, request)));
    }


    // event 삭제
    @DeleteMapping("/{event_id}")
    public ResponseEntity<? extends BasicResponse> deleteEvent(
            @PathVariable("event_id") Long id) {
        eventService.deleteEvent(id);
        return ResponseEntity.noContent().build();
    }
}
