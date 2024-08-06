package com.allin.teaming.workspace.controller;

import com.allin.teaming.Response.BasicResponse;
import com.allin.teaming.Response.DataResponse;
import com.allin.teaming.user.domain.User;
import com.allin.teaming.workspace.dto.*;
import com.allin.teaming.workspace.service.WorkspaceCreateService;
import com.allin.teaming.workspace.service.WorkspaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/workspaces")
@RequiredArgsConstructor
public class WorkspaceController {

    private final WorkspaceService workspaceService;
    private final WorkspaceCreateService workspaceCreateService;

    // 모든 워크 스페이스 조회
    @GetMapping
    public List<WorkspaceResponseDto> getAllWorkspaces() {
        return workspaceService.getAllWorkspaces();
    }


    // 아이디로 조회
    @GetMapping("/{workspace_id}")
    public ResponseEntity<WorkspaceResponseDto> getWorkspaceById(
            @PathVariable Long id) {
        return ResponseEntity.ok(workspaceService.getWorkspaceById(id));
    }

    // 워크스페이스 생성
    @PostMapping
    public ResponseEntity<WorkspaceResponseDto> createWorkspace(
            @RequestBody WorkspaceCreateRequestDto request,
            @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(workspaceCreateService.createWorkspace(token, request));
    }

    // 워크 스페이스 수정
    @PutMapping
    public ResponseEntity<WorkspaceResponseDto> updateWorkspace(
            @RequestBody WorkspaceDTO request) {
        return ResponseEntity.ok(workspaceService.updateWorkspace(request));
    }

    // 워크 스페이스 삭제
    @DeleteMapping("/{workspace_id}")
    public ResponseEntity<Void> deleteWorkspace(
            @PathVariable Long workspaceId) {
        workspaceService.deleteWorkspace(workspaceId);
        return ResponseEntity.noContent().build();
    }

    //사용자의 모든 워크 스페이스 조회
    @GetMapping("/user")
    public List<WorkspaceResponseDto> getAllWorkspacesByUserId(
            @RequestHeader("Authorization") String token) {
        return workspaceService.getAllWorkspacesByUserId(token);
    }

    // 워크 스페이스의 모든 멤버 조회
    @GetMapping("/{workspaceId}/members")
    public List<MembershipDTO> getAllMembersOfWorkspace(
            @PathVariable Long workspaceId) {
        return workspaceService.getAllMembersOfWorkspace(workspaceId);
    }

    // 워크스페이스에 멤버 추가
    @PostMapping("/{workspaceId}/users/{userId}")
    public ResponseEntity<WorkspaceResponseDto> addUserToWorkspace(
            @PathVariable("workspaceId") Long workspaceId,
            @PathVariable("userId") Long userId) {
        return ResponseEntity.ok(workspaceService.addUserToWorkspace(workspaceId, userId));
    }

    // 워크 스페이스에 멤버 삭제
    @DeleteMapping("/{workspaceId}/users/{userId}")
    public ResponseEntity<Void> removeUserFromWorkspace(
            @PathVariable Long workspaceId, @PathVariable Long userId) {
        workspaceService.removeUserFromWorkspace(workspaceId, userId);
        return ResponseEntity.noContent().build();
    }

    // TODO : 멤버들의 시간표 모두 조회
    @GetMapping("/schedule/{workspace_id}")
    public ResponseEntity<? extends BasicResponse> getAllScheduleInWorkspace(
            @PathVariable("workspace_id") Long workspace_id) {
        return ResponseEntity.ok(new DataResponse<>(workspaceService.getAllScheduleInWorkspace(workspace_id)));
    }



}