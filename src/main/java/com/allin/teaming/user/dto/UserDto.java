package com.allin.teaming.user.dto;

import com.allin.teaming.user.domain.RoleType;
import com.allin.teaming.user.domain.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

public class UserDto {
    // 회원 가입
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

        private Long schoolId;

        @NotBlank
        private String schoolName;

        @NotBlank
        private String gitId;

        @NotBlank
        private String notionMail;

        @NotBlank
        private String plusMail;

        @Email(message = "잘못된 이메일 형식입니다. ")
        private String email;

        @NotBlank
        private String major;

        @NotBlank
        private String birth;

        private String sns;

        public User toUser(String password) {
            return User.builder()
                    .username(username)
                    .phone(phone)
                    .password(password)
                    .role(role)
                    .schoolId(schoolId)
                    .schoolName(schoolName)
                    .gitId(gitId)
                    .notionMail(notionMail)
                    .plusMail(plusMail)
                    .email(email)
                    .major(major)
                    .birth(birth)
                    .sns(sns)
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
                    .schoolId(user.getSchoolId())
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
        private Long schoolId;
        private String major;
        private String birth;
        private String sns;

        public static UserDetailDto of(User user) {
            return UserDetailDto.builder()
                    .id(user.getId())
                    .email(user.getEmail())
                    .username(user.getUsername())
                    .schoolId(user.getSchoolId())
                    .major(user.getMajor())
                    .birth(user.getBirth())
                    .sns(user.getSns())
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

    // 사용자 정보 수정
    @Getter
    public static class UserModifyRequest {
        private String username;
        private String phone;
        private Long schoolId;
        private String schoolName;
        private String gitId;
        private String notionMail;
        private String plusMail;
        private String email;
        private String major;
        private String birth;
        private String sns;

        @Builder
        public UserModifyRequest(String username, String phone, Long schoolId, String schoolName,
                                 String gitId, String notionMail, String plusMail, String email,
                                 String major, String birth, String sns) {
            this.username = username;
            this.phone = phone;
            this.schoolId = schoolId;
            this.schoolName = schoolName;
            this.gitId = gitId;
            this.notionMail = notionMail;
            this.plusMail = plusMail;
            this.email = email;
            this.major = major;
            this.birth = birth;
            this.sns = sns;
        }
    }
}
