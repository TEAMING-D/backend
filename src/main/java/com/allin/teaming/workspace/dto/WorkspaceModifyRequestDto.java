package com.allin.teaming.workspace.dto;

import com.allin.teaming.user.domain.Membership;
import com.allin.teaming.workspace.domain.Workspace;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Builder
public class WorkspaceModifyRequestDto {
    private Long workspaceId;
    private String name;
    private String description;
    private String type; // 워크스페이스 유형 (수업, 대회, 동아리, 기타 등)
    private LocalDate deadline; // 마감 기한
    private List<Long> members; // 현재 팀원 ID 리스트

    public Workspace toEntity(List<Membership> members) {
        return Workspace.builder()
                .name(name)
                .description(description)
                .type(type)
                .deadline(deadline)
                .memberships(members)
                .build();
    }

}
