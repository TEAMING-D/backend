package com.allin.teaming.Archive.dto;

import com.allin.teaming.Archive.domain.Material;
import com.allin.teaming.user.dto.UserSimpleDto;
import lombok.Builder;

import java.net.URL;
import java.util.List;

@Builder
public class MaterialSimpleResponseDto {
    private Long materialId;
    private String filename;
    private URL url;
    private UserSimpleDto owner;
    private List<String> workNames;

    public static MaterialSimpleResponseDto toDto(Material material, List<String> workNames,
                                                  URL url, UserSimpleDto owner) {
        return MaterialSimpleResponseDto.builder()
                .materialId(material.getId())
                .filename(material.getFilename())
                .url(url)
                .owner(owner)
                .workNames(workNames)
                .build();
    }
}
