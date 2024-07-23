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
            @PathVariable("schedule_id") Long id) {
        return ResponseEntity.ok(new DataResponse<>(scheduleService.getScheduleById(id)));
    }

    // userId로 조회
    @GetMapping("/user/{user_id}")
    public ResponseEntity<? extends BasicResponse> getScheduleByUserId(
            @PathVariable("user_id") Long id) {
        return ResponseEntity.ok(new DataResponse<>(scheduleService.getScheduleByUserId(id)));
    }

    // schedule 생성
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<? extends BasicResponse> createSchedule(
            @Valid @RequestBody ScheduleCreateDto request) {
        return ResponseEntity.ok(new DataResponse<>(scheduleService.createSchedule(request)));
    }

    // schedule 수정
    @PatchMapping(value = "/{schedule_id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<? extends BasicResponse> modifySchedule(
            @PathVariable("schedule_id") Long id,
            @RequestBody ScheduleModifyDto request) {
        return ResponseEntity.ok(new DataResponse<>(scheduleService.modifySchedule(id, request)));
    }


    // schedule 삭제
    @DeleteMapping("/{schedule_id}")
    public ResponseEntity<? extends BasicResponse> deleteSchedule(
            @PathVariable("schedule_id") Long id) {
        scheduleService.deleteSchedule(id);
        return ResponseEntity.noContent().build();
    }
}
