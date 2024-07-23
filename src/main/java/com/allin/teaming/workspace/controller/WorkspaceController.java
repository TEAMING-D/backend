package com.allin.teaming.workspace.controller;

import com.allin.teaming.user.domain.Membership;
import com.allin.teaming.workspace.dto.WorkspaceDTO;
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

    @GetMapping
    public List<WorkspaceDTO> getAllWorkspaces() {
        return workspaceService.getAllWorkspaces();
    }

    @GetMapping("/{id}")
    public ResponseEntity<WorkspaceDTO> getWorkspaceById(@PathVariable Long id) {
        return ResponseEntity.ok(workspaceService.getWorkspaceById(id));
    }

    @PostMapping
    public ResponseEntity<WorkspaceDTO> createWorkspace(@RequestBody WorkspaceDTO workspaceDTO) {
        return ResponseEntity.ok(workspaceService.createWorkspace(workspaceDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<WorkspaceDTO> updateWorkspace(@PathVariable Long id, @RequestBody WorkspaceDTO workspaceDTO) {
        return ResponseEntity.ok(workspaceService.updateWorkspace(id, workspaceDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWorkspace(@PathVariable Long id) {
        workspaceService.deleteWorkspace(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/user/{userId}")
    public List<WorkspaceDTO> getAllWorkspacesByUserId(@PathVariable Long userId) {
        return workspaceService.getAllWorkspacesByUserId(userId);
    }

    @GetMapping("/{workspaceId}/members")
    public List<Membership> getAllMembersOfWorkspace(@PathVariable Long workspaceId) {
        return workspaceService.getAllMembersOfWorkspace(workspaceId);
    }
}
