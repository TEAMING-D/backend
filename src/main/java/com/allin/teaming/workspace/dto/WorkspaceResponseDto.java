package com.allin.teaming.workspace.dto;

import com.allin.teaming.workspace.domain.Workspace;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Builder
public class WorkspaceResponseDto {
    private Long id;
    private String name;
    private String description;
    private String type; // 워크스페이스 유형 (수업, 대회, 동아리, 기타 등)
    private LocalDate deadline; // 마감 기한
    private LocalDate createdDate;
    private List<MembershipDTO> members;


    public static WorkspaceResponseDto toDto(Workspace workspace, List<MembershipDTO> members) {
        return WorkspaceResponseDto.builder()
                .id(workspace.getId())
                .name(workspace.getName())
                .description(workspace.getDescription())
                .type(workspace.getType())
                .createdDate(workspace.getCreated_date())
                .deadline(workspace.getDeadline())
                .members(members)
                .build();
    }
}
