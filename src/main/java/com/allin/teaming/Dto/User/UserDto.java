package com.allin.teaming.Dto.User;

import com.allin.teaming.Domain.User.School;
import com.allin.teaming.Domain.User.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.modelmapper.ModelMapper;

public class UserDto {

    private static ModelMapper modelMapper = new ModelMapper();

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
        private String info;
        private Long schoolID;
        @Email(message = "잘못된 이메일 형식입니다. ")
        private String email;
        private String major;

        public User toUser(School school) {
            return User.builder()
                    .username(username)
                    .phone(phone)
                    .info(info)
                    .school(school)
                    .email(email)
                    .major(major)
                    .build();
        }

        public static UserRegistDto of(User user) {
            return new UserRegistDto(user.getUsername(), user.getPhone(), user.getInfo()
                    , user.getSchool().getId(), user.getEmail(), user.getMajor());
        }
    }

    // 조회
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserInfoDto {
        private Long id;
        private String email;
        private String username;

        private String info;
        private Long schoolID;
        private String major;

        public static UserInfoDto of(User user) {
            return modelMapper.map(user, UserInfoDto.class);}

        public UserInfoDto(User user) {
            this.id = user.getId();
            this.email = user.getEmail();
            this.username = user.getUsername();

            this.info = user.getInfo();
            this.schoolID = (user.getSchool() != null) ? user.getSchool().getId() : null;
            this.major = user.getMajor();
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
        private String username;
        private String phone;
        private String info;
        private Long schoolID;
        private String email;
        private String major;

        @Builder
        public UserModifyRequest(String username, String phone, String info,
                             Long schoolID, String email, String major) {
            this.username = username;
            this.phone = phone;
            this.info = info;
            this.schoolID = schoolID;
            this.email = email;
            this.major = major;
        }
    }

}
