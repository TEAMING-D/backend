package com.allin.teaming.workspace.dto.membership;

import com.allin.teaming.user.domain.Membership;
import com.allin.teaming.user.domain.User;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class MembershipResponseDto {

    private Long membershipId;     // Membership의 고유 ID
    private Long userId;           // 사용자 id
    private String username;       //사용자 이름

    public static MembershipResponseDto toDto(Membership membership, User user) {
        return MembershipResponseDto.builder()
                .membershipId(membership.getId())
                .userId(membership.getUser().getId())
                .username(membership.getUser().getUsername())
                .build();
    }
}


