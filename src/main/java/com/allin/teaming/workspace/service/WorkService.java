package com.allin.teaming.workspace.service;

import com.allin.teaming.workspace.domain.Assignment;
import com.allin.teaming.workspace.domain.Work;
import com.allin.teaming.workspace.domain.WorkStatus;
import com.allin.teaming.workspace.domain.Workspace;
import com.allin.teaming.workspace.dto.WorkDTO;
import com.allin.teaming.workspace.exception.WorkNotFoundException;
import com.allin.teaming.workspace.repository.AssignmentRepository;
import com.allin.teaming.workspace.repository.WorkRepository;
import com.allin.teaming.workspace.repository.WorkspaceRepository;
import com.allin.teaming.user.domain.Membership;
import com.allin.teaming.user.domain.User;
import com.allin.teaming.user.repository.MembershipRepository;
import com.allin.teaming.user.repository.UserRepository; // UserRepository 추가
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WorkService {

    private final WorkRepository workRepository;
    private final WorkspaceRepository workspaceRepository;
    private final MembershipRepository membershipRepository;
    private final AssignmentRepository assignmentRepository;
    private final UserRepository userRepository; // UserRepository 추가

    // 업무 생성
    @Transactional
    public WorkDTO createWork(Long workspaceId, WorkDTO workDTO) {
        // 1. 워크스페이스 조회
        Workspace workspace = workspaceRepository.findById(workspaceId)
                .orElseThrow(() -> new IllegalArgumentException("Workspace not found with id: " + workspaceId));

        // 2. 업무 생성
        Work work = new Work();
        work.setName(workDTO.getName());
        work.setDescription(workDTO.getDescription());
        work.setCreated_at(LocalDate.now());
        work.setDue_date(workDTO.getDueDate());
        work.setStatus(workDTO.getStatus());
        work.setProgress(workDTO.getProgress());
        work.setWorkspace(workspace); // 올바른 워크스페이스 설정

        work = workRepository.save(work);

        // 3. 유저 검증 및 업무에 유저 할당
        validateAndAssignUsers(workDTO.getAssignedUserIds(), workspaceId, work);

        // 4. 생성된 업무 반환
        return new WorkDTO(work);
    }

    // 업무 수정
    @Transactional
    public WorkDTO updateWork(Long workspaceId, Long id, WorkDTO workDTO) {
        // 업무를 조회
        Work work = workRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Work not found"));

        if (!work.getWorkspace().getId().equals(workspaceId)) {
            throw new IllegalArgumentException("Workspace ID does not match");
        }

        // 업무의 세부 사항을 업데이트
        work.setName(workDTO.getName());
        work.setDescription(workDTO.getDescription());
        work.setCreated_at(workDTO.getCreatedAt());
        work.setDue_date(workDTO.getDueDate());
        work.setStatus(workDTO.getStatus());
        work.setProgress(workDTO.getProgress());

        assignmentRepository.deleteByWork(work);
        validateAndAssignUsers(workDTO.getAssignedUserIds(), workspaceId, work);
        workRepository.save(work);
        return new WorkDTO(work);
    }


    // 업무 삭제
    @Transactional
    public void deleteWork(Long workspaceId, Long workId) {
        Work work = workRepository.findByIdAndWorkspaceId(workId, workspaceId)
                .orElseThrow(() -> new EntityNotFoundException("Work not found with id " + workId + " in workspace " + workspaceId));

        assignmentRepository.deleteByWork(work);
        workRepository.delete(work);
    }


    private void validateAndAssignUsers(List<Long> userIds, Long workspaceId, Work work) {
        for (Long userId : userIds) {
            // 유저 및 워크스페이스 조회
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));
            Workspace workspace = workspaceRepository.findById(workspaceId)
                    .orElseThrow(() -> new IllegalArgumentException("Workspace not found with id: " + workspaceId));

            // 워크스페이스 내에 존재하는 유저인지 확인
            Membership membership = membershipRepository.findByUserAndWorkspace(user, workspace)
                    .orElseThrow(() -> new IllegalArgumentException("User not found in workspace"));

            // 업무에 유저 할당
            Assignment assignment = new Assignment();
            assignment.setWork(work);
            assignment.setMembership(membership);
            assignmentRepository.save(assignment);
        }
    }
}
