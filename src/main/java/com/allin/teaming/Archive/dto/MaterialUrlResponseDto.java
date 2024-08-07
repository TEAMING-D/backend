package com.allin.teaming.Archive.dto;

import com.allin.teaming.Archive.domain.Material;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class MaterialUrlResponseDto {
    private Long MaterialId;
    private String getUrl;

    public static MaterialUrlResponseDto toDto(Material material, String getUrl) {
        return MaterialUrlResponseDto.builder()
                .MaterialId(material.getId())
                .getUrl(getUrl)
                .build();
    }
}
