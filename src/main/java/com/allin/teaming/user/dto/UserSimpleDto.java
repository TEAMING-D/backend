package com.allin.teaming.user.dto;

import com.allin.teaming.user.domain.User;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class UserSimpleDto {
    private Long userId;
    private String username;
    private String schoolName;
    private String schoolNum;

    public static UserSimpleDto of(User user) {
        return UserSimpleDto.builder()
                .userId(user.getId())
                .username(user.getUsername())
                .schoolName(user.getSchool().getName())
                .schoolNum(user.getSchoolNum())
                .build();
    }
}
