package com.allin.teaming.workspace.controller;

import com.allin.teaming.Response.BasicResponse;
import com.allin.teaming.Response.DataResponse;
import com.allin.teaming.user.dto.MeetingParticipantDto.*;
import com.allin.teaming.workspace.dto.MeetingDto.*;
import com.allin.teaming.workspace.service.MeetingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/meetings")
public class MeetingController {

    private final MeetingService meetingService;

    // id로 조회
    @GetMapping("/{meeting_id}")
    public ResponseEntity<? extends BasicResponse> getMeetingById(
            @PathVariable("meeting_id") Long id) {
        return ResponseEntity.ok(new DataResponse<>(meetingService.getMeetingById(id)));
    }

    // workspace id 로 전체 조회
    @GetMapping("/workspace/{workspace_id}")
    public ResponseEntity<? extends BasicResponse> getAllMeetingByWorkspaceId(
            @PathVariable("workspace_id") Long id) {
        return ResponseEntity.ok(new DataResponse<>(meetingService.getAllMeetingByWorkspaceId(id)));
    }

    // user id 로 전체 조회
    @GetMapping("/user/{user_id}")
    public ResponseEntity<? extends BasicResponse> getAllMeetingByUserId(
            @PathVariable("user_id") Long id) {
        return ResponseEntity.ok(new DataResponse<>(meetingService.getAllMeetingByUserId(id)));
    }

    // Title로 전체 조회
    @GetMapping("/title/{title}")
    public ResponseEntity<? extends BasicResponse> getMeetingByTitle(
            @PathVariable("title") String title) {
        return ResponseEntity.ok(new DataResponse<>(meetingService.getMeetingByTitle(title)));
    }

    // user의 id들을 받아서 가능한 회의 시간 전체 조회
    @GetMapping("/available")
    public ResponseEntity<? extends BasicResponse> getAvailableTimes(
            @RequestBody List<Long> userIds) {
        return ResponseEntity.ok(new DataResponse<>(meetingService.getAllAvailableMeetingTime(userIds)));
    }


    // 회의 생성
    @PostMapping
    public ResponseEntity<? extends BasicResponse> createMeeting(
            @RequestBody MeetingCreateDto request) {
        return ResponseEntity.ok(new DataResponse<>(meetingService.createMeeting(request)));
    }

    // 회의 시간 수정
    @PutMapping
    public ResponseEntity<? extends BasicResponse> modifyTimeMeeting(
            @RequestBody MeetingTimeModifyDto request) {
        return ResponseEntity.ok(new DataResponse<>(meetingService.modifyMeetingTime(request)));
    }

    // 회의 이름 수정
    @PutMapping
    public ResponseEntity<? extends BasicResponse> modifyTitleMeeting(
            @RequestBody MeetingTitleModifyDto request) {
        return ResponseEntity.ok(new DataResponse<>(meetingService.modifyMeetingTitle(request)));
    }

    // 회의 완료
    @PutMapping("/complete/{meeting_id}")
    public ResponseEntity<? extends BasicResponse> completeMeeting(
            @PathVariable("meeting_id") Long id) {
        return ResponseEntity.ok(new DataResponse<>(meetingService.completeMeeting(id)));
    }

    // 회의 삭제
    @DeleteMapping("/delete/{meeting_id}")
    public ResponseEntity<? extends BasicResponse> deleteMeeting(
            @PathVariable("meeting_id") Long meetingId){
        meetingService.deleteMeeting(meetingId);
        return ResponseEntity.noContent().build();
    }


    // 회의 참여자 삭제
    @DeleteMapping("/delete/participants")
    public ResponseEntity<? extends BasicResponse> deleteParticipant(
            @RequestBody MeetingParticipantDeleteDto request) {
        meetingService.deleteParticipants(request);
        return ResponseEntity.noContent().build();
    }
}