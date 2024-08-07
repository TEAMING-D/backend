package com.allin.teaming.Archive.dto;

import com.allin.teaming.Archive.domain.Material;
import com.allin.teaming.user.domain.User;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public class MaterialResponseDto {
    private Long materialId;
    private String username;
    private String schoolNum;
    private String filename;
    private double size;
    private LocalDate createdAt;

    public static MaterialResponseDto toDto(Material material) {
        MaterialResponseDtoBuilder builder = MaterialResponseDto.builder()
                .materialId(material.getId())
                .filename(material.getFilename())
                .size(material.getSize())
                .createdAt(material.getCreatedAt());
        User owner = material.getOwner();
        if (owner != null) {
            builder.username(owner.getUsername())
                    .schoolNum(owner.getSchoolNum());
        }
        return builder.build();
    }
}
