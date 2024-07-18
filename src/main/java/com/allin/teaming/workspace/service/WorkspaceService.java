package com.allin.teaming.workspace.service;

import com.allin.teaming.workspace.domain.Workspace;
import com.allin.teaming.workspace.dto.WorkspaceDTO;
import com.allin.teaming.workspace.exception.WorkspaceNotFoundException;
import com.allin.teaming.workspace.mapper.WorkspaceMapper;
import com.allin.teaming.workspace.repository.WorkspaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

// workspace 서비스 클래스
@Service
@Transactional
@RequiredArgsConstructor
public class WorkspaceService {

    private final WorkspaceRepository workspaceRepository;
    private final WorkspaceMapper workspaceMapper;

    /**
     * 모든 Workspace 조회
     * @return 모든 Workspace의 DTO 리스트
     */
    public List<WorkspaceDTO> getAllWorkspaces() {
        List<Workspace> workspaces = workspaceRepository.findAll();
        return workspaces.stream()
                .map(workspaceMapper::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * workspace의 id로 Workspace 조회
     * @throws WorkspaceNotFoundException id에 해당하는 Workspace가 없을 경우 예외 발생
     */
    public WorkspaceDTO getWorkspaceById(Long id) {
        Workspace workspace = findWorkspaceById(id);
        return workspaceMapper.toDTO(workspace);
    }

    // Workspace 생성
    public WorkspaceDTO createWorkspace(WorkspaceDTO workspaceDTO) {
        Workspace workspace = workspaceMapper.toEntity(workspaceDTO);
        Workspace savedWorkspace = workspaceRepository.save(workspace);
        return workspaceMapper.toDTO(savedWorkspace);
    }

    /**
     * Workspace 수정
     * @throws WorkspaceNotFoundException id에 해당하는 Workspace가 없을 경우 예외 발생
     */
    public WorkspaceDTO updateWorkspace(Long id, WorkspaceDTO workspaceDTO) {
        Workspace existingWorkspace = findWorkspaceById(id);
        workspaceMapper.updateFromDTO(workspaceDTO, existingWorkspace);
        Workspace updatedWorkspace = workspaceRepository.save(existingWorkspace);
        return workspaceMapper.toDTO(updatedWorkspace);
    }

    /**
     * 주어진 id(workspace)의 Workspace 삭제
     * @throws WorkspaceNotFoundException id에 해당하는 Workspace가 없을 경우 예외 발생
     */
    public void deleteWorkspace(Long id) {
        Workspace workspace = findWorkspaceById(id);
        workspaceRepository.delete(workspace);
    }

    // 주어진 userId로 모든 Workspace 조회
    public List<WorkspaceDTO> getAllWorkspacesByUserId(Long userId) {
        List<Workspace> workspaces = workspaceRepository.findAllByUserId(userId);
        return workspaces.stream()
                .map(workspaceMapper::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * 주어진 workspaceId의 모든 멤버 조회
     * @param workspaceId 조회할 Workspace의 id
     * @return 해당 Workspace의 모든 멤버의 DTO 리스트
     * @throws WorkspaceNotFoundException id에 해당하는 Workspace가 없을 경우 예외 발생
     */
    public List<WorkspaceDTO> getAllMembersOfWorkspace(Long workspaceId) {
        Workspace workspace = findWorkspaceById(workspaceId);
        return workspace.getMembers().stream()
                .map(workspaceMapper::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * 주어진 workspaceId의 모든 멤버의 스케줄 조회
     * @param workspaceId 조회할 Workspace의 id
     * @return 해당 Workspace의 모든 멤버의 스케줄의 DTO 리스트
     * @throws WorkspaceNotFoundException id에 해당하는 Workspace가 없을 경우 예외 발생
     */
    public List<ScheduleDTO> getAllMembersSchedules(Long workspaceId) {
        Workspace workspace = findWorkspaceById(workspaceId);
        return workspace.getMembers().stream()
                .flatMap(member -> member.getSchedule().stream())
                .map(scheduleMapper::toDTO) // ScheduleMapper 필요에 따라 추가
                .collect(Collectors.toList());
    }

    /**
     * 주어진 workspaceId의 모든 멤버의 시간표 추합 조회
     * @param workspaceId 조회할 Workspace의 id
     * @return 해당 Workspace의 모든 멤버의 시간표를 추합한 DTO 리스트
     * @throws WorkspaceNotFoundException id에 해당하는 Workspace가 없을 경우 예외 발생
     */
    public List<TimeTableDTO> aggregateAllMembersTimeTables(Long workspaceId) {
        Workspace workspace = findWorkspaceById(workspaceId);
        // 시간표를 추합하는 로직 구현
        // 예를 들어, workspace의 모든 멤버의 시간표를 합산하여 반환
        // 구현 필요
    }

    /**
     * 주어진 id로 Workspace를 조회하여 반환
     * @param id 조회할 Workspace의 id
     * @return 해당 id에 해당하는 Workspace 객체
     * @throws WorkspaceNotFoundException id에 해당하는 Workspace가 없을 경우 예외 발생
     */
    private Workspace findWorkspaceById(Long id) {
        return workspaceRepository.findById(id)
                .orElseThrow(() -> new WorkspaceNotFoundException("Workspace not found with id: " + id));
    }
}
