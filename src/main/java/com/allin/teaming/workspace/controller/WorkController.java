package com.allin.teaming.workspace.controller;

import com.allin.teaming.workspace.dto.WorkDTO;
import com.allin.teaming.workspace.service.WorkService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/workspaces/{workspaceId}/works")
@RequiredArgsConstructor
public class WorkController {

    private final WorkService workService;

    @PostMapping
    public ResponseEntity<WorkDTO> createWork(
            @PathVariable Long workspaceId,
            @RequestBody WorkDTO workDTO) {
        System.out.println("Received WorkDTO: " + workDTO);
        WorkDTO createdWork = workService.createWork(workspaceId, workDTO);
        return ResponseEntity.ok(createdWork);
    }

    @PutMapping("/{id}")
    public ResponseEntity<WorkDTO> updateWork(
            @PathVariable Long workspaceId,
            @PathVariable Long id,
            @RequestBody WorkDTO workDTO) {
        WorkDTO updatedWork = workService.updateWork(workspaceId, id, workDTO);
        return ResponseEntity.ok(updatedWork);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWork(
            @PathVariable Long workspaceId,
            @PathVariable Long id) {
        workService.deleteWork(workspaceId, id);
        return ResponseEntity.noContent().build();
    }
}
