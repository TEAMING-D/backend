package com.allin.teaming.workspace.service;

import com.allin.teaming.user.Jwt.JwtUtil;
import com.allin.teaming.user.dto.UserDto;
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

    private final JwtUtil jwtUtil;

    private User findUserByToken(String token) {
        return userRepository.findByEmail(jwtUtil.getEmail(token.split(" ")[1]))
                .orElseThrow(() -> new IllegalArgumentException("해당 회원을 조회할 수 없습니다."));
    }

    // 업무 생성
    public WorkDTO createWork(String token, Long workspaceId, WorkDTO workDTO) {
        // 기존 워크스페이스와 동일한 이름을 가진 업무가 있는지 확인
        List<Work> existingWorks = workRepository.findByWorkspaceId(workspaceId);
        for (Work existingWork : existingWorks) {
            if (existingWork.getName().equals(workDTO.getName())) {
                throw new IllegalArgumentException("Duplicate work name in the same workspace");
            }
        }

        // 업무 생성
        Work work = new Work();
        work.setName(workDTO.getName());
        work.setDescription(workDTO.getDescription());
        work.setCreated_at(LocalDate.now());
        work.setDue_date(workDTO.getDueDate());
        work.setStatus(workDTO.getStatus());
        work.setProgress(workDTO.getProgress());
        work.setWeight(workDTO.getWeight());

        Work savedWork = workRepository.save(work);

        // 업무와 사용자 간의 관계를 저장
        for (Long userId : workDTO.getAssignedUserIds()) {
            // 유저와 워크스페이스를 통해 Membership 찾기
            Membership membership = membershipRepository.findByUserIdAndWorkspaceId(userId, workspaceId)
                    .orElseThrow(() -> new IllegalArgumentException("Membership not found for userId: " + userId + " in workspace: " + workspaceId));

            Assignment assignment = new Assignment();
            assignment.setWork(savedWork);
            assignment.setMembership(membership);
            assignment.setScore(0); // 기본 점수는 0으로 설정, 필요에 따라 수정 가능

            assignmentRepository.save(assignment);
        }

        // 응답 DTO 생성
        List<UserDto.UserAssignmentDto> assignedUsers = workDTO.getAssignedUserIds().stream()
                .map(userId -> {
                    Membership membership = membershipRepository.findByUserIdAndWorkspaceId(userId, workspaceId)
                            .orElseThrow(() -> new IllegalArgumentException("Membership not found for userId: " + userId + " in workspace: " + workspaceId));
                    return UserDto.UserAssignmentDto.of(membership.getUser(), workspaceId);
                }).collect(Collectors.toList());

        WorkDTO responseDTO = new WorkDTO(savedWork);
        responseDTO.setAssignedUserIds(workDTO.getAssignedUserIds());
        responseDTO.setAssignedUsers(assignedUsers);

        return responseDTO;
    }




    // 업무 수정 (유저 추가, 삭제도 가능)
    @Transactional
    public WorkDTO updateWork(Long workId, Long workspaceId, WorkDTO workDTO) {
        // 기존 업무 조회
        Work work = workRepository.findById(workId)
                .orElseThrow(() -> new IllegalArgumentException("Work not found with id: " + workId));

        // 워크스페이스 설정 확인 및 업데이트
        Workspace workspace = workspaceRepository.findById(workspaceId)
                .orElseThrow(() -> new IllegalArgumentException("Workspace not found with id: " + workspaceId));
        work.setWorkspace(workspace); // 업무에 워크스페이스 설정

        // 업무 정보 업데이트
        work.setName(workDTO.getName());
        work.setDescription(workDTO.getDescription());
        work.setDue_date(workDTO.getDueDate());
        work.setStatus(workDTO.getStatus());
        work.setProgress(workDTO.getProgress());
        work.setWeight(workDTO.getWeight());

        Work updatedWork = workRepository.save(work);

        // 현재 할당된 유저의 ID 목록 가져오기
        List<Long> currentUserIds = assignmentRepository.findByWork(updatedWork).stream()
                .map(assignment -> assignment.getMembership().getUser().getId())
                .collect(Collectors.toList());

        // 새로 할당할 유저 정보 저장
        List<Long> newUserIds = workDTO.getAssignedUserIds();

        // 추가할 유저 찾기
        List<Long> usersToAdd = newUserIds.stream()
                .filter(userId -> !currentUserIds.contains(userId))
                .collect(Collectors.toList());

        // 삭제할 유저 찾기
        List<Long> usersToRemove = currentUserIds.stream()
                .filter(userId -> !newUserIds.contains(userId))
                .collect(Collectors.toList());

        // 기존 Assignment 삭제 (삭제할 유저에 대한 Assignment)
        for (Long userIdToRemove : usersToRemove) {
            Membership membership = membershipRepository.findByUserIdAndWorkspaceId(userIdToRemove, workspaceId)
                    .orElseThrow(() -> new IllegalArgumentException("Membership not found for userId: " + userIdToRemove + " in workspace: " + workspaceId));

            Assignment assignment = assignmentRepository.findByWorkAndMembership(updatedWork, membership)
                    .orElseThrow(() -> new EntityNotFoundException("Assignment not found for userId: " + userIdToRemove + " in workspace: " + workspaceId));

            assignmentRepository.delete(assignment);
        }

        // 새로 할당할 Assignment 저장
        for (Long userIdToAdd : usersToAdd) {
            Membership membership = membershipRepository.findByUserIdAndWorkspaceId(userIdToAdd, workspaceId)
                    .orElseThrow(() -> new IllegalArgumentException("Membership not found for userId: " + userIdToAdd + " in workspace: " + workspaceId));

            Assignment assignment = new Assignment();
            assignment.setWork(updatedWork);
            assignment.setMembership(membership);
            assignment.setScore(0); // 기본 점수는 0으로 설정, 필요에 따라 수정 가능

            assignmentRepository.save(assignment);
        }

        // 응답 DTO 생성
        List<UserDto.UserAssignmentDto> assignedUsers = workDTO.getAssignedUserIds().stream()
                .map(userId -> {
                    Membership membership = membershipRepository.findByUserIdAndWorkspaceId(userId, workspaceId)
                            .orElseThrow(() -> new IllegalArgumentException("Membership not found for userId: " + userId + " in workspace: " + workspaceId));
                    return UserDto.UserAssignmentDto.of(membership.getUser(), workspaceId);
                }).collect(Collectors.toList());

        WorkDTO responseDTO = new WorkDTO(updatedWork);
        responseDTO.setAssignedUserIds(workDTO.getAssignedUserIds());
        responseDTO.setAssignedUsers(assignedUsers);

        return responseDTO;
    }



    // 업무 삭제 (업무 자체를 삭제)
    @Transactional
    public void deleteWork(Long workspaceId, Long workId) {

        List<Assignment> assignments = assignmentRepository.findByWorkId(workId);
        if (assignments.isEmpty()) {
            throw new IllegalArgumentException("No assignments found for workId: " + workId);
        }

        Long actualWorkspaceId = assignments.stream()
                .findFirst()
                .map(assignment -> membershipRepository.findById(assignment.getMembership().getId())
                        .orElseThrow(() -> new IllegalArgumentException("Membership not found with id: " + assignment.getMembership().getId()))
                        .getWorkspace().getId())
                .orElseThrow(() -> new IllegalArgumentException("Workspace not found for workId: " + workId));

        if (!actualWorkspaceId.equals(workspaceId)) {
            throw new IllegalArgumentException("Workspace ID mismatch: expected " + actualWorkspaceId + " but got " + workspaceId);
        }

        assignmentRepository.deleteByWorkId(workId);
        workRepository.deleteById(workId);
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

        List<UserDto.UserAssignmentDto> assignedUsers = assignmentRepository.findByWork(work).stream()
                .map(assignment -> UserDto.UserAssignmentDto.of(assignment.getMembership().getUser(), work.getWorkspace().getId()))
                .collect(Collectors.toList());

        dto.setAssignedUserIds(assignedUserIds);
        dto.setAssignedUsers(assignedUsers);
        return dto;
    }

}