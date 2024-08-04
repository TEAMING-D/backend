package com.allin.teaming.workspace.controller;

import com.allin.teaming.workspace.dto.WorkDTO;
import com.allin.teaming.workspace.service.WorkService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/workspaces/{workspaceId}/works")
@RequiredArgsConstructor
public class WorkController {
    private final WorkService workService;

    @PostMapping
    public ResponseEntity<WorkDTO> createWork(
            @PathVariable Long workspaceId,
            @RequestBody WorkDTO workDTO,
            @RequestHeader("Authorization") String token) {
        WorkDTO createdWork = workService.createWork(token, workspaceId, workDTO);
        return ResponseEntity.ok(createdWork);
    }

    @PutMapping("/{workId}")
    public ResponseEntity<WorkDTO> updateWork(
            @PathVariable Long workspaceId,
            @PathVariable Long workId,
            @RequestBody WorkDTO workDTO) {
        WorkDTO updatedWork = workService.updateWork(workId, workspaceId, workDTO);
        return ResponseEntity.ok(updatedWork);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWork(
            @PathVariable Long workspaceId,
            @PathVariable Long id) {
        workService.deleteWork(workspaceId, id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<WorkDTO>> getAllWorks(@PathVariable Long workspaceId) {
        List<WorkDTO> works = workService.getAllWorks(workspaceId);
        return ResponseEntity.ok(works);
    }

    @GetMapping("/{id}")
    public ResponseEntity<WorkDTO> getWorkById(
            @PathVariable Long workspaceId,
            @PathVariable Long id) {
        WorkDTO work = workService.getWorkById(id);
        return ResponseEntity.ok(work);
    }
}
