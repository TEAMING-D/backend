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

        @Email(message = "잘못된 이메일 형식입니다. ")
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
                    .build();
        }
    }

    @Builder
    public static class UserSimpleDto {
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
        private String schoolName;
        private String schoolNum;
        private String major;
        private String birth;
        private String sns;
        private String collabTools;

        public static UserDetailDto of(User user) {
            return UserDetailDto.builder()
                    .userId(user.getId())
                    .email(user.getEmail())
                    .username(user.getUsername())
                    .info(user.getInfo())
                    .schoolName(user.getSchool().getName())
                    .schoolNum(user.getSchoolNum())
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
    public static class  UserModifyRequest {
        private Long userId;
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

        @Builder
        public UserModifyRequest(String username, String phone, Long schoolId, String schoolName, String gitId, String notionMail, String plusMail, String email, String major, String birth, String sns, String collabTools) {
            this.username = username;
            this.phone = phone;
            this.schoolId = schoolId;
            this.email = email;
            this.major = major;
            this.birth = birth;
            this.sns = sns;
            this.schoolName = schoolName;
            this.gitId = gitId;
            this.notionMail = notionMail;
            this.plusMail = plusMail;
            this.collabTools = collabTools;
        }
    }

}
