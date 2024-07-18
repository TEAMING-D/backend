package com.allin.teaming.user.controller;

import com.allin.teaming.Response.BasicResponse;
import com.allin.teaming.Response.DataResponse;
import com.allin.teaming.user.dto.UserDto.*;
import com.allin.teaming.user.service.UserService;
import lombok.RequiredArgsConstructor;
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

    // 전체 조회
    @GetMapping("/all")
    public ResponseEntity<? extends BasicResponse> getAllUser() {
        return ResponseEntity.ok(new DataResponse<List>(userService.getAllUserInfo()));
    }

    // 회원 정보 수정
    @PatchMapping("/{user_id}")
    public ResponseEntity<? extends BasicResponse> modifyUser(
            @PathVariable("user_id") Long user_id,
            @RequestBody UserModifyRequest request) {
        userService.userModify(user_id, request);
        return ResponseEntity.ok().build();
    }

    // 로그아웃

    // 회원 탈퇴
    @DeleteMapping(path = "/{user_id}")
    public ResponseEntity<? extends BasicResponse> deleteUser(
            @PathVariable("user_id") Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
