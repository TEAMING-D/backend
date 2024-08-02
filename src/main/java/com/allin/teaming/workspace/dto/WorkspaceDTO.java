package com.allin.teaming.workspace.dto;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class WorkspaceDTO {
    private Long id;
    private String name;
    private String description;
    private String type; // 워크스페이스 유형 (수업, 대회, 동아리, 기타 등)
    private LocalDate deadline; // 마감 기한
    private LocalDate createdDate;
    private List<Long> members; // 현재 팀원 ID 리스트

    // 기본 생성자
    public WorkspaceDTO() {
    }

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

