package com.allin.teaming.Controller.User;

import com.allin.teaming.Dto.Response.BasicResponse;
import com.allin.teaming.Dto.Response.DataResponse;
import com.allin.teaming.Dto.User.UserDto.*;
import com.allin.teaming.Service.User.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class JoinController {

    private final UserService userService;
    // 회원 가입
    // TODO: Spring Security
    @PostMapping(path = "/signUp", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<? extends BasicResponse> signUp(
            @Valid @RequestBody UserRegistDto request
    ) {
        return ResponseEntity.ok().body(
                new DataResponse<IdResponse>(userService.signUp(request)));
    }
}
