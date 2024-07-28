package com.allin.teaming.workspace.service;

import com.allin.teaming.workspace.domain.Assignment;
import com.allin.teaming.workspace.domain.Work;
import com.allin.teaming.user.domain.Membership;
import com.allin.teaming.workspace.dto.AssignmentDTO;
import com.allin.teaming.workspace.repository.AssignmentRepository;
import com.allin.teaming.workspace.repository.WorkRepository;
import com.allin.teaming.user.repository.MembershipRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class AssignmentService {

    private final AssignmentRepository assignmentRepository;
    private final WorkRepository workRepository;
    private final MembershipRepository membershipRepository;

    // 업무에 사용자 추가
    public AssignmentDTO addUserToWork(Long workId, Long membershipId) {
        Work work = workRepository.findById(workId)
                .orElseThrow(() -> new IllegalArgumentException("Work not found with id: " + workId));
        Membership membership = membershipRepository.findById(membershipId)
                .orElseThrow(() -> new IllegalArgumentException("Membership not found with id: " + membershipId));

        // Check if assignment already exists
        Optional<Assignment> existingAssignment = assignmentRepository.findByWorkAndMembership(work, membership);
        if (existingAssignment.isPresent()) {
            throw new IllegalArgumentException("Assignment already exists for workId: " + workId + " and membershipId: " + membershipId);
        }

        Assignment assignment = new Assignment();
        assignment.setWork(work);
        assignment.setMembership(membership);

        Assignment savedAssignment = assignmentRepository.save(assignment);
        return convertToDTO(savedAssignment);
    }

    // 업무 사용자 삭제
    public void removeUserFromWork(Long workId, Long membershipId) {
        Work work = workRepository.findById(workId)
                .orElseThrow(() -> new IllegalArgumentException("Work not found with id: " + workId));
        Membership membership = membershipRepository.findById(membershipId)
                .orElseThrow(() -> new IllegalArgumentException("Membership not found with id: " + membershipId));

        Assignment assignment = assignmentRepository.findByWorkAndMembership(work, membership)
                .orElseThrow(() -> new IllegalArgumentException("Assignment not found for workId: " + workId + " and membershipId: " + membershipId));

        assignmentRepository.delete(assignment);
    }

    // 참여도(score) 업데이트
    @Transactional
    public AssignmentDTO updateScore(Long workId, Long membershipId, int score) {
        if (score < 1 || score > 9) {
            throw new IllegalArgumentException("Score must be between 1 and 9");
        }

        Work work = workRepository.findById(workId)
                .orElseThrow(() -> new IllegalArgumentException("Work not found with id: " + workId));
        Membership membership = membershipRepository.findById(membershipId)
                .orElseThrow(() -> new IllegalArgumentException("Membership not found with id: " + membershipId));

        Assignment assignment = assignmentRepository.findByWorkAndMembership(work, membership)
                .orElseThrow(() -> new IllegalArgumentException("Assignment not found for workId: " + workId + " and membershipId: " + membershipId));

        assignment.setScore(score);
        Assignment updatedAssignment = assignmentRepository.save(assignment);
        return convertToDTO(updatedAssignment);
    }

    // DTO로 변환
    private AssignmentDTO convertToDTO(Assignment assignment) {
        AssignmentDTO dto = new AssignmentDTO();
        dto.setId(assignment.getId());
        dto.setWorkId(assignment.getWork().getId());
        dto.setMembershipId(assignment.getMembership().getId());
        dto.setScore(assignment.getScore());
        return dto;
    }
}
