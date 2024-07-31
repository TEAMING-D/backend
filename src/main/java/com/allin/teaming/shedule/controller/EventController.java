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
            @PathVariable("event_id") Long eventId) {
        return ResponseEntity.ok(
                new DataResponse<>(eventService.getEventById(eventId)));
    }

    // schedule id로 전체 조회
    @GetMapping("/schedule/{schedule_id}")
    public ResponseEntity<? extends BasicResponse> getAllEventByScheduleID(
            @PathVariable("schedule_id") Long scheduleId) {
        return ResponseEntity.ok(new DataResponse<>(eventService.getAllEventByScheduleId(scheduleId)));
    }

    // user id로 전체 조회
    @GetMapping
    public ResponseEntity<? extends BasicResponse> getAllEventByUserID(
            @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(new DataResponse<>(eventService.getAllEventByUser(token)));
    }

    // event 생성
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<? extends BasicResponse> createEvent(
            @RequestHeader("Authorization") String token,
            @Valid @RequestBody EventRegistDto request) {
        return ResponseEntity.ok().body(
                new DataResponse<>(eventService.createEvent(token, request)));
    }

    // event 수정
    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<? extends BasicResponse> modifyEvent(
            @RequestHeader("Authorization") String token,
            @RequestBody EventModifyDto request) {
        return ResponseEntity.ok(new DataResponse<>(eventService.modifyEvent(token, request)));
    }


    // event 삭제
    @DeleteMapping("/{event_id}")
    public ResponseEntity<? extends BasicResponse> deleteEvent(
            @RequestHeader("Authorization") String token,
            @PathVariable("event_id") Long id) {
        eventService.deleteEvent(token, id);
        return ResponseEntity.noContent().build();
    }
}
