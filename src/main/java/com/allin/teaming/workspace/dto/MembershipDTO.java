package com.allin.teaming.workspace.dto;

import com.allin.teaming.user.domain.Membership;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class MembershipDTO {

    private Long id;            // Membership의 고유 ID
    private Long userId;        // 사용자 ID
    private String username;      //사용자 이름
    private Long workspaceId;   // 워크스페이스 ID

    // 기본 생성자
    public MembershipDTO() {
    }

    public static MembershipDTO toDto(Membership membership) {
        return MembershipDTO.builder()
                .id(membership.getId())
                .userId(membership.getUser().getId())
                .username(membership.getUser().getUsername())
                .workspaceId(membership.getWorkspace().getId())
                .build();
    }

    // 필드를 초기화하는 생성자
    public MembershipDTO(Long id, Long userId, String username, Long workspaceId) {
        this.id = id;
        this.userId = userId;
        this.username = username;
        this.workspaceId = workspaceId;
    }
}
