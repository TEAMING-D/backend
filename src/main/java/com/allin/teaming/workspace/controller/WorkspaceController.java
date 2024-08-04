package com.allin.teaming.workspace.controller;

import com.allin.teaming.user.domain.User;
import com.allin.teaming.workspace.dto.WorkspaceCreateRequestDto;
import com.allin.teaming.workspace.dto.WorkspaceDTO;
import com.allin.teaming.workspace.dto.MembershipDTO;
import com.allin.teaming.workspace.dto.WorkspaceResponseDto;
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
    // 워크 스페이스 업데이트
    @PutMapping("/{workspace_id}")
    public ResponseEntity<WorkspaceResponseDto> updateWorkspace(
            @PathVariable Long workspaceId,
            @RequestBody WorkspaceDTO workspaceDTO) {
        return ResponseEntity.ok(workspaceService.updateWorkspace(workspaceId, workspaceDTO));
    }

    // 워크 스페이스 삭제
    @DeleteMapping("/{workspace_id}")
    public ResponseEntity<Void> deleteWorkspace(
            @PathVariable Long workspaceId) {
        workspaceService.deleteWorkspace(workspaceId);
        return ResponseEntity.noContent().build();
    }

    //사용자의모든 워크 스페이스 조회
    @GetMapping("/user")
    public List<WorkspaceResponseDto> getAllWorkspacesByUserId(
            @RequestHeader("Authorization") String token) {
        return workspaceService.getAllWorkspacesByUser(token);
    }

    // 워크 스페이스의 모든 멤버 조회
    @GetMapping("/{workspaceId}/members")
    public List<MembershipDTO> getAllMembersOfWorkspace(
            @PathVariable Long workspaceId) {
        return workspaceService.getAllMembersOfWorkspace(workspaceId);
    }

    // 워크스페이스에 멤버 추가
    @PostMapping("/{workspaceId}/users/{userId}")
    public ResponseEntity<Void> addUserToWorkspace(
            @PathVariable Long workspaceId,
            @PathVariable Long userId) {
        workspaceService.addUserToWorkspace(workspaceId, userId);
        return ResponseEntity.noContent().build();
    }

    // 워크스페이스에 여러 멤버를 한번에 추가
    @PostMapping("/{workspaceId}/users")
    public ResponseEntity<Void> addUsersToWorkspace(
            @PathVariable Long workspaceId,
            @RequestBody List<Long> userIds) {
        workspaceService.addUsersToWorkspace(workspaceId, userIds);
        return ResponseEntity.noContent().build();
    }

    // 워크 스페이스에 멤버 삭제
    @DeleteMapping("/{workspaceId}/users/{userId}")
    public ResponseEntity<Void> removeUserFromWorkspace(
            @PathVariable Long workspaceId, @PathVariable Long userId) {
        workspaceService.removeUserFromWorkspace(workspaceId, userId);
        return ResponseEntity.noContent().build();
    }
}
