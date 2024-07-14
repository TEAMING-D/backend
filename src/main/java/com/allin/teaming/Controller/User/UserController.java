package com.allin.teaming.Controller.User;

import com.allin.teaming.Dto.Response.BasicResponse;
import com.allin.teaming.Dto.Response.DataResponse;
import com.allin.teaming.Dto.User.UserDto.*;
import com.allin.teaming.Service.User.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
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

    // 회원 가입
    // TODO: Spring Security
    @PostMapping(path = "/signUp", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<? extends BasicResponse> signUp(
            @Valid @RequestBody UserRegistDto request
    ) {
        return ResponseEntity.ok().body(
                new DataResponse<IdResponse>(userService.signUp(request)));
    }

    // 로그인
    // 로그아웃

    // 회원 탈퇴
    @DeleteMapping(path = "/{user_id}")
    public ResponseEntity<? extends BasicResponse> deleteUser(
            @PathVariable("user_id") Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
