package com.allin.teaming.workspace.service;

import com.allin.teaming.shedule.domain.Schedule;
import com.allin.teaming.shedule.dto.ScheduleDto.*;
import com.allin.teaming.user.Jwt.JwtUtil;
import com.allin.teaming.user.domain.User;
import com.allin.teaming.workspace.domain.Workspace;
import com.allin.teaming.workspace.dto.WorkspaceDTO;
import com.allin.teaming.workspace.dto.MembershipDTO;
import com.allin.teaming.user.domain.Membership;
import com.allin.teaming.workspace.dto.WorkspaceResponseDto;
import com.allin.teaming.workspace.exception.WorkspaceNotFoundException;
import com.allin.teaming.workspace.repository.WorkspaceRepository;
import com.allin.teaming.user.repository.UserRepository;
import com.allin.teaming.user.repository.MembershipRepository;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
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

    private User findUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저를 찾을 수 없습니다. "));
    }

    // 모든 Workspace 조회
    public List<WorkspaceResponseDto> getAllWorkspaces() {
        List<Workspace> workspaces = workspaceRepository.findAll();
        return workspaces.stream()
                .map(workspace -> {
                    List<MembershipDTO> members = getAllMembersOfWorkspace(workspace.getId());
                    return WorkspaceResponseDto.toDto(workspace, members);
                })
                .collect(Collectors.toList());
    }


    // workspace의 id로 Workspace 조회
    public WorkspaceResponseDto getWorkspaceById(Long id) {
        Workspace workspace = findWorkspaceById(id);
        List<MembershipDTO> members = getAllMembersOfWorkspace(id);
        return WorkspaceResponseDto.toDto(workspace, members);
    }


    // 주어진 id(Workspace)로 Workspace를 조회하여 반환
    private Workspace findWorkspaceById(Long id) {
        return workspaceRepository.findById(id)
                .orElseThrow(() -> new WorkspaceNotFoundException("Workspace not found with id: " + id));
    }

    // Workspace 생성
    @Transactional
    public WorkspaceDTO createWorkspace(String token, WorkspaceDTO workspaceDTO) {

        User user = findUserByToken(token);
        Workspace workspace = convertToEntity(workspaceDTO);
        workspace.setCreated_date(LocalDate.now());

        Workspace savedWorkspace = workspaceRepository.save(workspace);

        List<Long> users = workspaceDTO.getMembers();
        users.add(user.getId());
        System.out.println("<<<<<<" + users + ">>>>>>>");

        // 팀원 추가
        addInitialMembers(savedWorkspace, users); // 초기 팀원 추가 메서드


        return convertToDTO(savedWorkspace);
    }

    // 워크스페이스 수정
    @Transactional
    public WorkspaceResponseDto updateWorkspace(WorkspaceDTO workspaceDTO) {
        Workspace existingWorkspace = findWorkspaceById(workspaceDTO.getWorkspaceId());
        updateEntityFromDTO(workspaceDTO, existingWorkspace);

        // 기존 멤버 제거
        membershipRepository.deleteAllByWorkspace(existingWorkspace);

        if (workspaceDTO.getMembers() != null && !workspaceDTO.getMembers().isEmpty()) {
            addInitialMembers(existingWorkspace, workspaceDTO.getMembers());
        }

        Workspace updatedWorkspace = workspaceRepository.save(existingWorkspace);

        // 수정된 워크스페이스와 멤버를 기반으로 DTO 생성
        List<MembershipDTO> members = getAllMembersOfWorkspace(updatedWorkspace.getId());
        return WorkspaceResponseDto.toDto(updatedWorkspace, members);
    }


    // 주어진 id(workspace)의 Workspace 삭제
    public void deleteWorkspace(Long id) {
        Workspace workspace = findWorkspaceById(id);
        workspaceRepository.delete(workspace);
    }

    // 워크스페이스에 유저 추가
    @Transactional
    public WorkspaceResponseDto addUserToWorkspace(Long workspaceId, Long userId) {
        Workspace workspace = findWorkspaceById(workspaceId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));

        if (membershipRepository.existsByWorkspaceIdAndUserId(workspaceId, userId)) {
            throw new IllegalArgumentException("이미 존재하는 회원입니다.");
        }
        Membership membership = new Membership(user, workspace);
        membershipRepository.save(membership);
        List<MembershipDTO> members = getAllMembersOfWorkspace(workspace.getId());
        return WorkspaceResponseDto.toDto(workspace, members);
    }

    // 워크스페이스에서 유저 제거
    @Transactional
    public void removeUserFromWorkspace(Long workspaceId, Long userId) {
        Workspace workspace = findWorkspaceById(workspaceId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));

        Membership membership = membershipRepository.findByUserAndWorkspace(user, workspace)
                .orElseThrow(() -> new IllegalArgumentException("Membership not found for userId: " + userId + " in workspaceId: " + workspaceId));

        membershipRepository.delete(membership);
    }

    // 나의 모든 Workspace 조회
    @Transactional(readOnly = true)
    public List<WorkspaceResponseDto> getAllWorkspacesByUserId(String token) {
        User user = findUserByToken(token);
        List<Workspace> workspaces = membershipRepository.findAllByUser(user).stream()
                .map(Membership::getWorkspace)
                .collect(Collectors.toList());

        return workspaces.stream()
                .map(workspace -> {
                    List<MembershipDTO> members = getAllMembersOfWorkspace(workspace.getId());
                    return WorkspaceResponseDto.toDto(workspace, members);
                })
                .collect(Collectors.toList());
    }


    // 주어진 workspaceId의 모든 멤버 조회
    @Transactional(readOnly = true)
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
        dto.setWorkspaceId((workspace.getId()));
        dto.setName(workspace.getName());
        dto.setDescription(workspace.getDescription());
        dto.setType(workspace.getType());
        dto.setDeadline(workspace.getDeadline());
        dto.setCreatedDate(workspace.getCreated_date());
        dto.setMembers(memberIds); // 현재 팀원 설정
        return dto;
    }

    // WorkspaceDTO를 Workspace 엔티티로 변환하는 메서드
    private Workspace convertToEntity(WorkspaceDTO workspaceDTO) {
        Workspace workspace = new Workspace();
        workspace.setId(workspaceDTO.getWorkspaceId());
        workspace.setName(workspaceDTO.getName());
        workspace.setDescription(workspaceDTO.getDescription());
        workspace.setType(workspaceDTO.getType());
        workspace.setDeadline(workspaceDTO.getDeadline());
        workspace.setCreated_date(workspaceDTO.getCreatedDate());
        return workspace;
    }

    // WorkspaceDTO의 내용을 기존 Workspace 엔티티에 복사하는 메서드
    private void updateEntityFromDTO(WorkspaceDTO workspaceDTO, Workspace workspace) {
        workspace.setName(workspaceDTO.getName());
        workspace.setDescription(workspaceDTO.getDescription());
        workspace.setType(workspaceDTO.getType());
        workspace.setDeadline(workspaceDTO.getDeadline());
        workspace.setCreated_date(workspaceDTO.getCreatedDate());
    }

    // TODO : 멤버들의 시간표 모두 조회 테스트 해야함
    @Transactional(readOnly = true)
    public List<ScheduleDetailDto> getAllScheduleInWorkspace(Long workspaceId) {
        Workspace workspace = findWorkspaceById(workspaceId);
        List<User> members = workspace.getMembers().stream().map(Membership::getUser).toList();
        return members.stream()
                .map(User::getSchedule)
                .map(ScheduleDetailDto::of)
                .toList();
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
                membership.getUser().getUsername(),
                membership.getWorkspace().getId()
        );
    }
}