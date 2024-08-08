package com.allin.teaming.workspace.dto;

import com.allin.teaming.workspace.domain.Workspace;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class WorkspaceSimpleResponseDto {
    private Long workspaceId;
    private String workspaceName;

    public static WorkspaceSimpleResponseDto toDto(Workspace workspace) {
        return WorkspaceSimpleResponseDto.builder()
                .workspaceId(workspace.getId())
                .workspaceName(workspace.getName())
                .build();
    }
}
