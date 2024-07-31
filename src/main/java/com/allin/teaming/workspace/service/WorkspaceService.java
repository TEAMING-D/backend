package com.allin.teaming.workspace.service;

import com.allin.teaming.user.Jwt.JwtUtil;
import com.allin.teaming.user.domain.User;
import com.allin.teaming.workspace.domain.Workspace;
import com.allin.teaming.workspace.dto.WorkspaceDTO;
import com.allin.teaming.workspace.dto.MembershipDTO;
import com.allin.teaming.user.domain.Membership;
import com.allin.teaming.workspace.exception.WorkspaceNotFoundException;
import com.allin.teaming.workspace.repository.WorkspaceRepository;
import com.allin.teaming.user.repository.UserRepository;
import com.allin.teaming.user.repository.MembershipRepository;
import java.util.ArrayList;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class WorkspaceService {

    private final WorkspaceRepository workspaceRepository;
    private final UserRepository userRepository;
    private final MembershipRepository membershipRepository;

    private final JwtUtil jwtUtil;

    private User findUserByToken(String token) {
        return userRepository.findByEmail(jwtUtil.getEmail(token.split(" ")[1]))
            .orElseThrow(() -> new IllegalArgumentException("해당 회원을 조회할 수 없습니다."));
    }

    // 모든 Workspace 조회
    public List<WorkspaceDTO> getAllWorkspaces() {
        List<Workspace> workspaces = workspaceRepository.findAll();
        return workspaces.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // workspace의 id로 Workspace 조회
    public WorkspaceDTO getWorkspaceById(Long id) {
        Workspace workspace = findWorkspaceById(id);
        return convertToDTO(workspace);
    }

    // 주어진 id(Workspace)로 Workspace를 조회하여 반환
    private Workspace findWorkspaceById(Long id) {
        return workspaceRepository.findById(id)
                .orElseThrow(() -> new WorkspaceNotFoundException("Workspace not found with id: " + id));
    }

    // Workspace 생성
    public WorkspaceDTO createWorkspace(String token, WorkspaceDTO workspaceDTO) {
        User user = findUserByToken(token);
        Workspace workspace = convertToEntity(workspaceDTO);

        List<Long> users = workspaceDTO.getMembers();
        users.add(user.getId());

        Workspace savedWorkspace = workspaceRepository.save(workspace);

        // 팀원 추가
        addInitialMembers(savedWorkspace, users); // 초기 팀원 추가 메서드

        return convertToDTO(savedWorkspace);
    }

    // 워크스페이스 수정
    public WorkspaceDTO updateWorkspace(Long id, WorkspaceDTO workspaceDTO) {
        Workspace existingWorkspace = findWorkspaceById(id);
        updateEntityFromDTO(workspaceDTO, existingWorkspace);

        // 기존 멤버 제거
        membershipRepository.deleteAllByWorkspace(existingWorkspace);

        if (workspaceDTO.getMembers() != null && !workspaceDTO.getMembers().isEmpty()) {
            addInitialMembers(existingWorkspace, workspaceDTO.getMembers());
        }
        Workspace updatedWorkspace = workspaceRepository.save(existingWorkspace);
        return convertToDTO(updatedWorkspace);
    }

    // 주어진 id(workspace)의 Workspace 삭제
    public void deleteWorkspace(Long id) {
        Workspace workspace = findWorkspaceById(id);
        workspaceRepository.delete(workspace);
    }

    // 워크스페이스에 유저 추가
    public void addUserToWorkspace(Long workspaceId, Long userId) {
        Workspace workspace = findWorkspaceById(workspaceId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));

        Membership membership = new Membership(user, workspace);
        membershipRepository.save(membership);
    }

    // 워크스페이스에서 유저 제거
    public void removeUserFromWorkspace(Long workspaceId, Long userId) {
        Workspace workspace = findWorkspaceById(workspaceId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));

        Membership membership = membershipRepository.findByUserAndWorkspace(user, workspace)
                .orElseThrow(() -> new IllegalArgumentException("Membership not found for userId: " + userId + " in workspaceId: " + workspaceId));

        membershipRepository.delete(membership);
    }

    // 주어진 userId로 모든 Workspace 조회
    public List<WorkspaceDTO> getAllWorkspacesByUserId(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));
        List<Workspace> workspaces = membershipRepository.findAllByUser(user).stream()
                .map(Membership::getWorkspace)
                .collect(Collectors.toList());
        return workspaces.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // 주어진 workspaceId의 모든 멤버 조회
    public List<MembershipDTO> getAllMembersOfWorkspace(Long workspaceId) {
        Workspace workspace = findWorkspaceById(workspaceId);
        return workspace.getMembers().stream()
                .map(this::convertToMembershipDTO)
                .collect(Collectors.toList());
    }

    // Workspace를 WorkspaceDTO로 변환하는 메서드
    private WorkspaceDTO convertToDTO(Workspace workspace) {
        List<Long> memberIds = workspace.getMembers().stream()
                .map(membership -> membership.getUser().getId())
                .collect(Collectors.toList());

        WorkspaceDTO dto = new WorkspaceDTO();
        dto.setId(workspace.getId());
        dto.setName(workspace.getName());
        dto.setDescription(workspace.getDescription());
        dto.setType(workspace.getType()); // 유형 설정
        dto.setDeadline(workspace.getDeadline()); // 마감 기한 설정
        dto.setMembers(memberIds); // 현재 팀원 설정
        return dto;
    }

    // WorkspaceDTO를 Workspace 엔티티로 변환하는 메서드
    private Workspace convertToEntity(WorkspaceDTO workspaceDTO) {
        Workspace workspace = new Workspace();
        List<Membership> members = new ArrayList<>();
        workspace.setId(workspaceDTO.getId());
        workspace.setName(workspaceDTO.getName());
        workspace.setDescription(workspaceDTO.getDescription());
        workspace.setType(workspaceDTO.getType()); // 유형 설정
        workspace.setDeadline(workspaceDTO.getDeadline()); // 마감 기한 설정
        return workspace;
    }

    // WorkspaceDTO의 내용을 기존 Workspace 엔티티에 복사하는 메서드
    private void updateEntityFromDTO(WorkspaceDTO workspaceDTO, Workspace workspace) {
        workspace.setName(workspaceDTO.getName());
        workspace.setDescription(workspaceDTO.getDescription());
        workspace.setType(workspaceDTO.getType()); // 유형 설정
        workspace.setDeadline(workspaceDTO.getDeadline()); // 마감 기한 설정
    }

    // 초기 팀원 추가 메서드
    private void addInitialMembers(Workspace workspace, List<Long> initialMemberIds) {
        for (Long userId : initialMemberIds) {
            addUserToWorkspace(workspace.getId(), userId);
        }
    }

    private MembershipDTO convertToMembershipDTO(Membership membership) {
        return new MembershipDTO(
                membership.getId(),
                membership.getUser().getId(),
                membership.getWorkspace().getId()
        );
    }
}
