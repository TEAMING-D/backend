package com.allin.teaming.workspace.controller;

import com.allin.teaming.workspace.dto.AssignmentDTO;
import com.allin.teaming.workspace.service.AssignmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/assignments")
@RequiredArgsConstructor
public class AssignmentController {

    private final AssignmentService assignmentService;

    @PostMapping
    public ResponseEntity<AssignmentDTO> addUserToWork(
            @RequestParam Long workId,
            @RequestParam Long membershipId) {
        AssignmentDTO assignmentDTO = assignmentService.addUserToWork(workId, membershipId);
        return ResponseEntity.ok(assignmentDTO);
    }

    @DeleteMapping
    public ResponseEntity<Void> removeUserFromWork(
            @RequestParam Long workId,
            @RequestParam Long membershipId) {
        assignmentService.removeUserFromWork(workId, membershipId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/score")
    public ResponseEntity<AssignmentDTO> updateScore(
            @RequestParam Long workId,
            @RequestParam Long membershipId,
            @RequestParam int score) {
        AssignmentDTO updatedAssignment = assignmentService.updateScore(workId, membershipId, score);
        return ResponseEntity.ok(updatedAssignment);
    }
}
