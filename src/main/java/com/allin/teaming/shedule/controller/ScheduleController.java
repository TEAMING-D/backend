package com.allin.teaming.shedule.controller;

import com.allin.teaming.Response.BasicResponse;
import com.allin.teaming.Response.DataResponse;
import com.allin.teaming.shedule.dto.ScheduleDto.*;
import com.allin.teaming.shedule.service.ScheduleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/schedules")
@RequiredArgsConstructor
public class ScheduleController {
    private final ScheduleService scheduleService;

    // id로 조회
    @GetMapping("/{schedule_id}")
    public ResponseEntity<? extends BasicResponse> getScheduleById(
            @PathVariable("schedule_id") Long scheduleId) {
        return ResponseEntity.ok(new DataResponse<>(scheduleService.getScheduleById(scheduleId)));
    }

    // 내 시간표 조회
    @GetMapping
    public ResponseEntity<? extends BasicResponse> getSchedule(
            @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(new DataResponse<>(scheduleService.getScheduleByUser(token)));
    }

    // userId로 조회
    @GetMapping("/user/{user_id}")
    public ResponseEntity<? extends BasicResponse> getScheduleByUserId(
        @PathVariable("user_id") Long userId) {
        return ResponseEntity.ok(new DataResponse<>(scheduleService.getScheduleByUserId(userId)));
    }

    // schedule 생성
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<? extends BasicResponse> createSchedule(
            @RequestHeader("Authorization") String token,
            @Valid @RequestBody ScheduleCreateDto request) {
        return ResponseEntity.ok(new DataResponse<>(scheduleService.createSchedule(token, request)));
    }

    // schedule 수정
    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<? extends BasicResponse> modifySchedule(
            @RequestHeader("Authorization") String token,
            @RequestBody ScheduleModifyDto request) {
        return ResponseEntity.ok(new DataResponse<>(scheduleService.modifySchedule(token, request)));
    }


    // schedule 삭제
    @DeleteMapping
    public ResponseEntity<? extends BasicResponse> deleteSchedule(
            @RequestHeader("Authorization") String token) {
        scheduleService.deleteSchedule(token);
        return ResponseEntity.noContent().build();
    }
}
