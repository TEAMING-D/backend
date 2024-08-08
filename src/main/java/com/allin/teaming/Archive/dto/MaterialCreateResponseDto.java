package com.allin.teaming.Archive.dto;

import com.allin.teaming.Archive.domain.Material;
import com.allin.teaming.user.dto.UserSimpleDto;
import com.allin.teaming.workspace.dto.WorkspaceSimpleResponseDto;
import lombok.Builder;
import lombok.Getter;

import java.net.URL;
import java.time.LocalDate;
import java.util.List;

// 생성 api 요청 시, Prisigned-url 함께 반환
@Builder
@Getter
public class MaterialCreateResponseDto {
    private Long materialId;
    private String filename;
    private URL url;
    private UserSimpleDto owner;
    private WorkspaceSimpleResponseDto workspace;
    private List<String> workNames;
    private double size;
    private LocalDate createdAt;

    public static MaterialCreateResponseDto toDto(Material material, List<String> workNames,
                                                  URL url, UserSimpleDto owner, WorkspaceSimpleResponseDto workspace) {
        return MaterialCreateResponseDto.builder()
                .materialId(material.getId())
                .filename(material.getFilename())
                .url(url)
                .owner(owner)
                .workspace(workspace)
                .workNames(workNames)
                .size(material.getSize())
                .createdAt(material.getCreatedAt())
                .build();
    }
}
