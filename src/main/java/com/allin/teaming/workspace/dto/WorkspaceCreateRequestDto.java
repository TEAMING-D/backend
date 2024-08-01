package com.allin.teaming.workspace.dto;

import com.allin.teaming.workspace.domain.Workspace;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Builder
public class WorkspaceCreateRequestDto {
    private String name;
    private String description;
    private String type; // 예: 수업, 대회, 동아리, 기타

    private LocalDate deadline;
    private LocalDate created_date;
    private List<Long> members;

    public Workspace toEntity() {
        return Workspace.builder()
                .name(name)
                .description(description)
                .deadline(deadline)
                .type(type)
                .created_date(LocalDate.now())
                .build();
    }

}
