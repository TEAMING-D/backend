package com.allin.teaming.Archive.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class MaterialUpdateWorkRequestDto {
    private Long materialId;
    private List<Long> workIds;
}
