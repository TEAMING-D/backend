package com.allin.teaming.workspace.service;

import com.allin.teaming.workspace.domain.Assignment;
import com.allin.teaming.workspace.domain.Work;
import com.allin.teaming.workspace.domain.WorkStatus;
import com.allin.teaming.workspace.domain.Workspace;
import com.allin.teaming.workspace.dto.WorkDTO;
import com.allin.teaming.workspace.dto.AssignmentDTO;
import com.allin.teaming.workspace.repository.AssignmentRepository;
import com.allin.teaming.workspace.repository.WorkRepository;
import com.allin.teaming.workspace.repository.WorkspaceRepository;
import com.allin.teaming.user.domain.Membership;
import com.allin.teaming.user.domain.User;
import com.allin.teaming.user.repository.MembershipRepository;
import com.allin.teaming.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WorkService {

    private final WorkRepository workRepository;
    private final WorkspaceRepository workspaceRepository;
    private final MembershipRepository membershipRepository;
    private final AssignmentRepository assignmentRepository;
    private final UserRepository userRepository;

    // 업무 생성
    @Transactional
    public WorkDTO createWork(Long workspaceId, WorkDTO workDTO) {
        Workspace workspace = workspaceRepository.findById(workspaceId)
                .orElseThrow(() -> new IllegalArgumentException("Workspace not found with id: " + workspaceId));

        Work work = new Work();
        work.setName(workDTO.getName());
        work.setDescription(workDTO.getDescription());
        work.setCreated_at(LocalDate.now());
        work.setDue_date(workDTO.getDueDate());
        work.setStatus(workDTO.getStatus());
        work.setProgress(workDTO.getProgress());
        work.setWeight(workDTO.getWeight()); // 가중치 설정
        work.setWorkspace(workspace);

        work = workRepository.save(work);

        validateAndAssignUsers(workDTO.getAssignedUserIds(), workspaceId, work);

        return convertToDTO(work);
    }

    // 업무 수정 (유저 추가, 삭제도 가능)
    @Transactional
    public WorkDTO updateWork(Long workspaceId, Long id, WorkDTO workDTO) {
        Work work = workRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Work not found"));

        if (!work.getWorkspace().getId().equals(workspaceId)) {
            throw new IllegalArgumentException("Workspace ID does not match");
        }

        work.setName(workDTO.getName());
        work.setDescription(workDTO.getDescription());
        work.setCreated_at(workDTO.getCreatedAt());
        work.setDue_date(workDTO.getDueDate());
        work.setStatus(workDTO.getStatus());
        work.setProgress(workDTO.getProgress());
        work.setWeight(workDTO.getWeight()); // 가중치 업데이트

        assignmentRepository.deleteByWork(work);
        validateAndAssignUsers(workDTO.getAssignedUserIds(), workspaceId, work);
        workRepository.save(work);

        return convertToDTO(work);
    }

    // 업무 삭제 (업무 자체를 삭제)
    @Transactional
    public void deleteWork(Long workspaceId, Long workId) {
        Work work = workRepository.findByIdAndWorkspaceId(workId, workspaceId)
                .orElseThrow(() -> new EntityNotFoundException("Work not found with id " + workId + " in workspace " + workspaceId));

        assignmentRepository.deleteByWork(work);
        workRepository.delete(work);
    }

    // 워크스페이스 내의 모든 업무 조회
    @Transactional(readOnly = true)
    public List<WorkDTO> getAllWorks(Long workspaceId) {
        List<Work> works = workRepository.findByWorkspaceId(workspaceId);
        return works.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public WorkDTO getWorkById(Long workId) {
        Work work = workRepository.findById(workId)
                .orElseThrow(() -> new EntityNotFoundException("Work not found with id: " + workId));
        return convertToDTO(work);
    }

    private void validateAndAssignUsers(List<Long> userIds, Long workspaceId, Work work) {
        for (Long userId : userIds) {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));
            Workspace workspace = workspaceRepository.findById(workspaceId)
                    .orElseThrow(() -> new IllegalArgumentException("Workspace not found with id: " + workspaceId));

            Membership membership = membershipRepository.findByUserAndWorkspace(user, workspace)
                    .orElseThrow(() -> new IllegalArgumentException("User not found in workspace"));

            Assignment assignment = new Assignment();
            assignment.setWork(work);
            assignment.setMembership(membership);
            assignmentRepository.save(assignment);
        }
    }

    @Transactional
    public WorkDTO updateWeight(Long workId, int weight) {
        if (weight < 1 || weight > 9) {
            throw new IllegalArgumentException("Weight must be between 1 and 9");
        }

        Work work = workRepository.findById(workId)
                .orElseThrow(() -> new EntityNotFoundException("Work not found"));

        work.setWeight(weight);
        workRepository.save(work);

        return convertToDTO(work);
    }

    @Transactional
    public AssignmentDTO updateScore(Long workId, Long membershipId, int score) {
        if (score < 1 || score > 9) {
            throw new IllegalArgumentException("Score must be between 1 and 9");
        }

        Work work = workRepository.findById(workId)
                .orElseThrow(() -> new EntityNotFoundException("Work not found"));
        Membership membership = membershipRepository.findById(membershipId)
                .orElseThrow(() -> new EntityNotFoundException("Membership not found"));

        Assignment assignment = assignmentRepository.findByWorkAndMembership(work, membership)
                .orElseThrow(() -> new EntityNotFoundException("Assignment not found"));

        assignment.setScore(score);
        assignmentRepository.save(assignment);

        return convertToDTO(assignment);
    }

    private AssignmentDTO convertToDTO(Assignment assignment) {
        AssignmentDTO dto = new AssignmentDTO();
        dto.setId(assignment.getId());
        dto.setWorkId(assignment.getWork().getId());
        dto.setMembershipId(assignment.getMembership().getId());
        dto.setScore(assignment.getScore());
        return dto;
    }

    private WorkDTO convertToDTO(Work work) {
        WorkDTO dto = new WorkDTO(work);
        List<Long> assignedUserIds = assignmentRepository.findByWork(work).stream()
                .map(assignment -> assignment.getMembership().getUser().getId())
                .collect(Collectors.toList());
        dto.setAssignedUserIds(assignedUserIds);
        return dto;
    }
}
