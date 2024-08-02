package com.allin.teaming.workspace.controller;

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

    @GetMapping
    public List<WorkspaceResponseDto> getAllWorkspaces() {
        return workspaceService.getAllWorkspaces();
    }

    @GetMapping("/{id}")
    public ResponseEntity<WorkspaceResponseDto> getWorkspaceById(@PathVariable Long id) {
        return ResponseEntity.ok(workspaceService.getWorkspaceById(id));
    }

    @PostMapping
    public ResponseEntity<WorkspaceResponseDto> createWorkspace(
            @RequestBody WorkspaceCreateRequestDto request,
            @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(workspaceCreateService.createWorkspace(token, request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<WorkspaceResponseDto> updateWorkspace(@PathVariable Long id, @RequestBody WorkspaceDTO workspaceDTO) {
        return ResponseEntity.ok(workspaceService.updateWorkspace(id, workspaceDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWorkspace(@PathVariable Long id) {
        workspaceService.deleteWorkspace(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/user/{userId}")
    public List<WorkspaceResponseDto> getAllWorkspacesByUserId(@RequestHeader("Authorization") String token) {
        return workspaceService.getAllWorkspacesByUserId(token);
    }

    @GetMapping("/{workspaceId}/members")
    public List<MembershipDTO> getAllMembersOfWorkspace(@PathVariable Long workspaceId) {
        return workspaceService.getAllMembersOfWorkspace(workspaceId);
    }

    @PostMapping("/{workspaceId}/users/{userId}")
    public ResponseEntity<Void> addUserToWorkspace(
            @PathVariable Long workspaceId,
            @PathVariable Long userId) {
        workspaceService.addUserToWorkspace(workspaceId, userId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{workspaceId}/users/{userId}")
    public ResponseEntity<Void> removeUserFromWorkspace(@PathVariable Long workspaceId, @PathVariable Long userId) {
        workspaceService.removeUserFromWorkspace(workspaceId, userId);
        return ResponseEntity.noContent().build();
    }
}
