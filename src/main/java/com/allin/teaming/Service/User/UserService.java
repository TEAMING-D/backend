package com.allin.teaming.Service.User;

import com.allin.teaming.Domain.User.User;
import com.allin.teaming.Dto.UserDto.*;
import com.allin.teaming.Repository.User.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    // id로 회원 조회 (마이페이지)
    @Transactional(readOnly = true)
    private UserInfoDto findUserById(Long userId) {
        return userRepository.findById(userId)
                .map(UserInfoDto::of)
                .orElseThrow(() -> new IllegalArgumentException("해당 회원이 존재하지 않습니다."));
    }

    // 이메일로 회원조회
    @Transactional(readOnly = true)
    private UserInfoDto findUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(UserInfoDto::of)
                .orElseThrow(() -> new IllegalArgumentException("해당 회원이 존재하지 않습니다."));
    }

    // 회원 전체 조회
    @Transactional(readOnly = true)
    public List<UserInfoDto> getAllUserInfo() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(UserInfoDto::new).collect(Collectors.toList());
    }

    // 팀원 검색 (username 으로 회원 조회)

    // 사용자 정보 입력(수정)
    @Transactional
    public void userUpdate(Long id) {

    }

    // 회원가입

    // 탈퇴

    // 로그인

    // 로그아웃


}

