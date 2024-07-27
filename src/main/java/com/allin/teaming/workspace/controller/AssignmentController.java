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

    // 업무에 사용자 추가
    @PostMapping
    public ResponseEntity<AssignmentDTO> addUserToWork(
            @RequestParam Long workId,
            @RequestParam Long membershipId) {
        AssignmentDTO assignmentDTO = assignmentService.addUserToWork(workId, membershipId);
        return ResponseEntity.ok(assignmentDTO);
    }

    // 업무 사용자 삭제
    @DeleteMapping
    public ResponseEntity<Void> removeUserFromWork(
            @RequestParam Long workId,
            @RequestParam Long membershipId) {
        assignmentService.removeUserFromWork(workId, membershipId);
        return ResponseEntity.noContent().build();
    }
}
