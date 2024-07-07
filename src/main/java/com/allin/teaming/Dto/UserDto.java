package com.allin.teaming.Dto;

import com.allin.teaming.Domain.User.User;
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
        private String username;
        private String password;
        private String email;
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
            this.schoolID = user.getSchool().getId();
            this.major = user.getMajor();
        }
    }

    // 수정
    public static class UserModifyDto {
        private String username;
        private String info;
        private String school;
    }

}
