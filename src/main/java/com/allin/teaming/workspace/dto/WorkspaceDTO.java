package com.allin.teaming.workspace.dto;

import com.allin.teaming.user.domain.Membership;
import com.allin.teaming.workspace.domain.Workspace;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class WorkspaceDTO {
    private Long workspaceId;
    private String name;
    private String description;
    private String type; // 워크스페이스 유형 (수업, 대회, 동아리, 기타 등)
    private LocalDate deadline; // 마감 기한
    private LocalDate createdDate;
    private List<Long> members; // 현재 팀원 ID 리스트

    // 기본 생성자
    public WorkspaceDTO() {
    }

//    public WorkspaceDTO toDto(Workspace workspace) {
//        return WorkspaceDTO.builder()
//                .workspaceId(workspace.getId())
//                .name(workspace.getName())
//                .description(workspace.getDescription())
//                .type(workspace.getType())
//                .deadline(workspace.getDeadline())
//                .createdDate(workspace.getCreated_date())
//                // 이거 유저 아이디로 바꿔야 되는거 아닌지..?
//                .members(workspace.getMembers().stream().map(Membership::getId).toList())
//                .build();
//    }

    // 필드를 초기화하는 생성자
//    public WorkspaceDTO(Long id, String name, String description, String type, LocalDate deadline, List<Long> members) {
//        this.id = id;
//        this.name = name;
//        this.description = description;
//        this.type = type;
//        this.deadline = deadline;
//        this.members = members;
//    }
}

