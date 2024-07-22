package com.allin.teaming.workspace.service;

import com.allin.teaming.workspace.domain.Workspace;
import com.allin.teaming.workspace.dto.WorkspaceDTO;
import com.allin.teaming.user.domain.Membership;
import com.allin.teaming.workspace.exception.WorkspaceNotFoundException;
import com.allin.teaming.workspace.repository.WorkspaceRepository;
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

    /**
     * 모든 Workspace 조회
     * @return 모든 Workspace의 DTO 리스트
     */
    public List<WorkspaceDTO> getAllWorkspaces() {
        List<Workspace> workspaces = workspaceRepository.findAll();
        return workspaces.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * workspace의 id로 Workspace 조회
     * @throws WorkspaceNotFoundException id에 해당하는 Workspace가 없을 경우 예외 발생
     */
    public WorkspaceDTO getWorkspaceById(Long id) {
        Workspace workspace = findWorkspaceById(id);
        return convertToDTO(workspace);
    }

    /**
     * 주어진 id(Workspace)로 Workspace를 조회하여 반환
     * @throws WorkspaceNotFoundException id에 해당하는 Workspace가 없을 경우 예외 발생
     */
    private Workspace findWorkspaceById(Long id) {
        return workspaceRepository.findById(id)
                .orElseThrow(() -> new WorkspaceNotFoundException("Workspace not found with id: " + id));
    }

    // Workspace 생성
    public WorkspaceDTO createWorkspace(WorkspaceDTO workspaceDTO) {
        Workspace workspace = convertToEntity(workspaceDTO);
        Workspace savedWorkspace = workspaceRepository.save(workspace);
        return convertToDTO(savedWorkspace);
    }

    /**
     * Workspace 수정
     * @throws WorkspaceNotFoundException id에 해당하는 Workspace가 없을 경우 예외 발생
     */
    public WorkspaceDTO updateWorkspace(Long id, WorkspaceDTO workspaceDTO) {
        Workspace existingWorkspace = findWorkspaceById(id);
        updateEntityFromDTO(workspaceDTO, existingWorkspace);
        Workspace updatedWorkspace = workspaceRepository.save(existingWorkspace);
        return convertToDTO(updatedWorkspace);
    }

    /**
     * 주어진 id(workspace)의 Workspace 삭제
     * @throws WorkspaceNotFoundException id에 해당하는 Workspace가 없을 경우 예외 발생
     */
    public void deleteWorkspace(Long id) {
        Workspace workspace = findWorkspaceById(id);
        workspaceRepository.delete(workspace);
    }

    /**
     * 주어진 userId로 모든 Workspace 조회
     */
    public List<WorkspaceDTO> getAllWorkspacesByUserId(Long userId) {
        List<Workspace> workspaces = workspaceRepository.findAllByUserid(userId);
        return workspaces.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * 주어진 workspaceId의 모든 멤버 조회
     * @return 해당 Workspace의 모든 멤버의 DTO 리스트
     * @throws WorkspaceNotFoundException id에 해당하는 Workspace가 없을 경우 예외 발생
     */
    public List<Membership> getAllMembersOfWorkspace(Long workspaceId) {
        Workspace workspace = findWorkspaceById(workspaceId);
        return workspace.getMembers();
    }

    // Workspace를 WorkspaceDTO로 변환하는 메서드
    private WorkspaceDTO convertToDTO(Workspace workspace) {
        WorkspaceDTO dto = new WorkspaceDTO();
        dto.setId(workspace.getId());
        dto.setName(workspace.getName());
        dto.setDescription(workspace.getDescription());
        return dto;
    }

    // WorkspaceDTO를 Workspace 엔티티로 변환하는 메서드
    private Workspace convertToEntity(WorkspaceDTO workspaceDTO) {
        Workspace workspace = new Workspace();
        workspace.setId(workspaceDTO.getId());
        workspace.setName(workspaceDTO.getName());
        workspace.setDescription(workspaceDTO.getDescription());
        return workspace;
    }

    // WorkspaceDTO의 내용을 기존 Workspace 엔티티에 복사하는 메서드
    private void updateEntityFromDTO(WorkspaceDTO workspaceDTO, Workspace workspace) {
        workspace.setName(workspaceDTO.getName());
        workspace.setDescription(workspaceDTO.getDescription());
    }
}
