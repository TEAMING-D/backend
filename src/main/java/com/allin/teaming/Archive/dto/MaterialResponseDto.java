package com.allin.teaming.Archive.dto;

import com.allin.teaming.Archive.domain.Material;
import com.allin.teaming.user.domain.User;
import com.allin.teaming.user.dto.UserSimpleDto;
import com.allin.teaming.workspace.dto.WorkspaceSimpleResponseDto;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Builder
@Getter
public class MaterialResponseDto {
    private Long materialId;
    private UserSimpleDto owner;
    private WorkspaceSimpleResponseDto workspace;
    private String filename;
    private double size;
    private LocalDate createdAt;

    public static MaterialResponseDto toDto(Material material, UserSimpleDto owner, WorkspaceSimpleResponseDto workspace) {
        return MaterialResponseDto.builder()
                .materialId(material.getId())
                .owner(owner)
                .workspace(workspace)
                .filename(material.getFilename())
                .size(material.getSize())
                .createdAt(material.getCreatedAt())
                .build();
    }
}
