package com.allin.teaming.Dto;

import lombok.Getter;
import lombok.Setter;

public class UserDto {

    @Getter @Setter
    public static class UserRegist {
        private String username;
        private String password;
        private String email;
    }
}
