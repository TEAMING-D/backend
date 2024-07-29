package com.allin.teaming.user.controller;

import com.allin.teaming.Response.BasicResponse;
import com.allin.teaming.Response.DataResponse;
import com.allin.teaming.user.dto.UserDto.*;
import com.allin.teaming.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    // 조회
    // id로 조회
    @GetMapping("/{user_id}")
    public ResponseEntity<? extends BasicResponse> getUserById(
            @PathVariable("user_id") Long id) {
        return ResponseEntity.ok(
                new DataResponse<>(userService.getUserInfoById(id)));
    }

    // 이메일로 조회
    @GetMapping("/email/{email}")
    public ResponseEntity<? extends BasicResponse> getUserByEmail(
            @PathVariable("email") String email) {
        return ResponseEntity.ok(new DataResponse<>(userService.getUserByEmail(email)));
    }

    // 이름으로 조회
    @GetMapping("/username/{username}")
    public ResponseEntity<? extends BasicResponse> getUserByUsername(
            @PathVariable("username") String username) {
        return ResponseEntity.ok(new DataResponse<>(userService.getUserByUsername(username)));
    }

    // 학번으로 조회
    @GetMapping("/schoolNum/{schoolNum}")
    public ResponseEntity<? extends BasicResponse> getUserBySchoolNum(
            @PathVariable("schoolNum") String schoolNum) {
        return ResponseEntity.ok(new DataResponse<>(userService.getUserBySchoolNum(schoolNum)));
    }


    // 전체 조회
    @GetMapping("/all")
    public ResponseEntity<? extends BasicResponse> getAllUser() {
        return ResponseEntity.ok(new DataResponse<>(userService.getAllUserInfo()));
    }

    // 회원 정보 수정
    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<? extends BasicResponse> modifyUser(
            @RequestBody UserModifyRequest request) {
        return ResponseEntity.ok(new DataResponse<>(userService.userModify(request)));
    }

    // 로그아웃

    // 회원 탈퇴
    @DeleteMapping("/{user_id}")
    public ResponseEntity<? extends BasicResponse> deleteUser(
            @PathVariable("user_id") Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
