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
        private String schoolName;
        private String schoolNum;

        @Email(message = "잘못된 이메일 형식입니다.")
        private String email;
        private String major;

        @NotBlank
        private String birth;

        private String sns;
        private String gitId;
        private String notionMail;
        private String plusMail;
        private String collabTools; // 추가된 필드

        public User toUser(School school, String password) {
            return User.builder()
                    .username(username)
                    .phone(phone)
                    .password(password)
                    .role(role)
                    .info(info)
                    .school(school)
                    .schoolNum(schoolNum)
                    .email(email)
                    .major(major)
                    .birth(birth)
                    .sns(sns)
                    .gitId(gitId)
                    .notionMail(notionMail)
                    .plusMail(plusMail)
                    .collabTools(collabTools)
                    .build();
        }
    }

    // 사용자 상세 조회
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class UserDetailDto {
        private Long userId;
        private String email;
        private String username;
        private String info;
        private String schoolName;
        private String schoolNum;
        private String phone;
        private String major;
        private String birth;
        private String sns;

        private String gitId;
        private String notionMail;
        private String plusMail;
        private String collabTools;

        public static UserDetailDto of(User user) {
            return UserDetailDto.builder()
                    .userId(user.getId())
                    .email(user.getEmail())
                    .username(user.getUsername())
                    .info(user.getInfo())
                    .schoolName(user.getSchool().getName())
                    .schoolNum(user.getSchoolNum())
                    .phone(user.getPhone())
                    .major(user.getMajor())
                    .birth(user.getBirth())
                    .sns(user.getSns())
                    .gitId(user.getGitId())
                    .notionMail(user.getNotionMail())
                    .plusMail(user.getPlusMail())
                    .collabTools(user.getCollabTools())
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
    public static class IdResponse {
        private Long id;

        public static IdResponse of(User user) {
            return new IdResponse(user.getId());
        }
    }

    // 사용자 정보 수정 요청
    @Getter
    public static class UserModifyRequest {
        private String username;
        private String info;
        private String schoolName;
        private String schoolNum;
        private String phone;
        private Long schoolId;
        private String gitId;
        private String notionMail;
        private String plusMail;
        private String email;
        private String major;
        private String birth;
        private String sns;
        private String collabTools;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserAssignmentDto {
        private Long userId;
        private String username;
        private Long workspaceId;

        public static UserAssignmentDto of(User user, Long workspaceId) {
            return UserAssignmentDto.builder()
                    .userId(user.getId())
                    .username(user.getUsername())
                    .workspaceId(workspaceId)
                    .build();
        }
    }

}