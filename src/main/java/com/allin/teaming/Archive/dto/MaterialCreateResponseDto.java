package com.allin.teaming.Archive.dto;

import com.allin.teaming.Archive.domain.Material;
import com.allin.teaming.user.dto.UserSimpleDto;
import lombok.Builder;
import lombok.Getter;

import java.net.URL;
import java.util.List;

// 생성 api 요청 시, Prisigned-url 함께 반환
@Builder
@Getter
public class MaterialCreateResponseDto {
    private Long materialId;
    private String filename;
    private URL url;
    private UserSimpleDto owner;
    private List<String> workNames;
    private double size;

    public static MaterialCreateResponseDto toDto(Material material, List<String> workNames,
                                                  URL url, UserSimpleDto owner) {
        return MaterialCreateResponseDto.builder()
                .materialId(material.getId())
                .filename(material.getFilename())
                .url(url)
                .owner(owner)
                .workNames(workNames)
                .size(material.getSize())
                .build();
    }
}
