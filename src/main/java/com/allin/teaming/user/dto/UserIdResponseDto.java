package com.allin.teaming.user.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class UserIdResponseDto {
    private String username;
    private Long userId;
}
