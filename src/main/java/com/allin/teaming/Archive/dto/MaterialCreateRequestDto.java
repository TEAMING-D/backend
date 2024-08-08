package com.allin.teaming.Archive.dto;

import com.allin.teaming.Archive.domain.Material;
import com.allin.teaming.user.domain.Membership;
import com.allin.teaming.user.domain.User;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
public class MaterialCreateRequestDto {
    private String filename;
    private double size;
    private List<Long> workIds;
    private Long workspaceId;
    public Material toEntity(Membership membership) {
        return Material.builder()
                .filename(filename)
                .size(size)
                .createdAt(LocalDate.now())
                .membership(membership)
                .build();
    }
}
