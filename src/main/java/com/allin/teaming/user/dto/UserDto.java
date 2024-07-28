package com.allin.teaming.user.dto;

import com.allin.teaming.user.domain.RoleType;
import com.allin.teaming.user.domain.School;
import com.allin.teaming.user.domain.User;

import jakarta.validation.constraints.Email;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

public class UserDto {
    // 회원 가입
    // TODO : 학교 이름으로 수정하기
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class UserRegistDto {
        @NotBlank
        private String username;

        @NotBlank
        private String phone;

        @Builder.Default
        private RoleType role = RoleType.USER;

        @NotBlank
        private String password;

        private String info;
        private Long schoolId;

        @Email(message = "잘못된 이메일 형식입니다. ")
        private String email;
        private String major;

        public User toUser(School school, String password) {
            return User.builder()
                    .username(username)
                    .phone(phone)
                    .password(password)
                    .role(role)
                    .info(info)
                    .school(school)
                    .email(email)
                    .major(major)
                    .build();
        }
    }

    @Builder
    public static class UserSimpleDto {
        private Long id;
        private String username;
        private Long schoolId;

        public static UserSimpleDto of(User user) {
            return UserSimpleDto.builder()
                    .id(user.getId())
                    .username(user.getUsername())
                    .schoolId(user.getSchool().getId())
                    .build();
        }

    }

    // 조회
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class UserDetailDto {
        private Long id;
        private String email;
        private String username;

        private String info;
        private Long schoolId;
        private String major;

        public static UserDetailDto of(User user) {
            return UserDetailDto.builder()
                    .id(user.getId())
                    .email(user.getEmail())
                    .username(user.getUsername())
                    .info(user.getInfo())
                    .schoolId(user.getSchool().getId())
                    .major(user.getMajor())
                    .build();
        }
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserScheduleDto {
        private Long id;
        private String username;
        private Long scheduleId;

        public static UserScheduleDto of(User user) {
            return UserScheduleDto.builder()
                    .id(user.getId())
                    .username(user.getUsername())
                    .scheduleId(user.getSchedule().getId())
                    .build();
        }
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class IdResponse{
        private Long id;
        public static IdResponse of(User user) {
            return new IdResponse(user.getId());
        }
    }

    // 사용자 정보 수정
    @Getter
    public static class UserModifyRequest {
        private Long userId;
        private String username;
        private String info;
        private Long schoolId;
        private String major;
    }

}
