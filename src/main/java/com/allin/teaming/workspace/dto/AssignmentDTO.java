package com.allin.teaming.workspace.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AssignmentDTO {

    private Long id;
    private Long workId;
    private Long membershipId;
    private Long userId; // 유저의 아이디
    private String userName; // 유저의 이름
    private Long workspaceId; // 유저의 워크스페이스 아이디
    private int score;
}
