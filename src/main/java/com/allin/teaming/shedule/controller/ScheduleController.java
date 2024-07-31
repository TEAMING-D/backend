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

    // userId로 조회
    @GetMapping("/user")
    public ResponseEntity<? extends BasicResponse> getScheduleByUserId(
            @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(new DataResponse<>(scheduleService.getScheduleByUser(token)));
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
            @RequestBody ScheduleModifyDto request) {
        return ResponseEntity.ok(new DataResponse<>(scheduleService.modifySchedule(request)));
    }


    // schedule 삭제
    @DeleteMapping("/{schedule_id}")
    public ResponseEntity<? extends BasicResponse> deleteSchedule(
            @PathVariable("schedule_id") Long scheduleId) {
        scheduleService.deleteSchedule(scheduleId);
        return ResponseEntity.noContent().build();
    }
}
