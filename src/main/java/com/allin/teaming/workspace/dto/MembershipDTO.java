package com.allin.teaming.workspace.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MembershipDTO {

    private Long id;            // Membership의 고유 ID
    private Long userId;        // 사용자 ID
    private Long workspaceId;   // 워크스페이스 ID

    // 기본 생성자
    public MembershipDTO() {
    }

    // 필드를 초기화하는 생성자
    public MembershipDTO(Long id, Long userId, Long workspaceId) {
        this.id = id;
        this.userId = userId;
        this.workspaceId = workspaceId;
    }
}
