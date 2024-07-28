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

    // 회원 조회
    @GetMapping("/{user_id}")
    public ResponseEntity<? extends BasicResponse> getUserById(@PathVariable("user_id") Long userId) {
        UserDetailDto user = userService.getUserInfoById(userId);
        return ResponseEntity.ok().body(new DataResponse<>(user));
    }

    // 이메일로 회원조회
    @GetMapping("/email/{email}")
    public ResponseEntity<? extends BasicResponse> getUserByEmail(@PathVariable("email") String email) {
        UserDetailDto user = userService.getUserByEmail(email);
        return ResponseEntity.ok().body(new DataResponse<>(user));
    }

    // 회원 전체 조회
    @GetMapping("/all")
    public ResponseEntity<? extends BasicResponse> getAllUserInfo() {
        List<UserDetailDto> users = userService.getAllUserInfo();
        return ResponseEntity.ok().body(new DataResponse<>(users));
    }

    // 팀원 검색 (username 으로 회원 조회)
    @GetMapping("/username/{username}")
    public ResponseEntity<? extends BasicResponse> getUserByUsername(@PathVariable("username") String username) {
        UserDetailDto user = userService.getUserByUsername(username);
        return ResponseEntity.ok().body(new DataResponse<>(user));
    }

    // 회원가입
    @PostMapping(value = "/signup", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<? extends BasicResponse> signUp(@RequestBody UserRegistDto userRegistDto) {
        IdResponse id = userService.signUp(userRegistDto);
        return ResponseEntity.ok().body(new DataResponse<>(id));
    }

    // 사용자 정보 입력(수정)
    @PatchMapping("/{user_id}")
    public ResponseEntity<? extends BasicResponse> modifyUser(@PathVariable("user_id") Long userId,
                                                              @RequestBody UserModifyRequest request) {
        IdResponse id = userService.userModify(userId, request);
        return ResponseEntity.ok().body(new DataResponse<>(id));
    }

    // 탈퇴
    @DeleteMapping("/{user_id}")
    public ResponseEntity<? extends BasicResponse> deleteUser(@PathVariable("user_id") Long userId) {
        userService.deleteUser(userId);
        return ResponseEntity.ok().build();
    }
}
