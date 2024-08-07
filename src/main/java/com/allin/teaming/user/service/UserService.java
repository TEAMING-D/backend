package com.allin.teaming.user.service;

import com.allin.teaming.user.Jwt.JwtUtil;
import com.allin.teaming.user.domain.School;
import com.allin.teaming.user.domain.User;
import com.allin.teaming.user.dto.UserDto.*;
import com.allin.teaming.user.dto.UserIdResponseDto;
import com.allin.teaming.user.dto.UserSimpleDto;
import com.allin.teaming.user.repository.SchoolRepository;
import com.allin.teaming.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final SchoolRepository schoolRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JwtUtil jwtUtil;

    private User findUserByToken(String token) {
        return userRepository.findByEmail(jwtUtil.getEmail(token.split(" ")[1]))
                .orElseThrow(() -> new IllegalArgumentException("해당 회원을 조회할 수 없습니다."));
    }

    // id로 회원 조회 (마이페이지)
    @Transactional(readOnly = true)
    public UserDetailDto getUserInfo(String token) {
        return UserDetailDto.of(findUserByToken(token));
    }

    @Transactional(readOnly = true)
    public UserDetailDto getUserInfoById(Long userId) {
        return userRepository.findById(userId).map(UserDetailDto::of)
                .orElseThrow(() -> new IllegalArgumentException("해당 회원을 조회할 수 없습니다."));
    }

    // 이메일로 회원조회
    @Transactional(readOnly = true)
    public UserDetailDto getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(UserDetailDto::of)
                .orElseThrow(() -> new IllegalArgumentException("해당 회원이 존재하지 않습니다."));
    }

    // 회원 전체 조회
    @Transactional(readOnly = true)
    public List<UserDetailDto> getAllUserInfo() {
        return userRepository.findAll().stream()
                .map(UserDetailDto::of)
                .collect(Collectors.toList());
    }

    // 팀원 검색 (username 으로 회원 전체 조회)
    @Transactional(readOnly = true)
    public List<UserSimpleDto> getUserByUsername(String username) {
        return userRepository.findByUsername(username).stream()
                .map(UserSimpleDto::of).toList();
    }

    // 학번으로 조회
    @Transactional(readOnly = true)
    public UserDetailDto getUserBySchoolNum(String schoolNum) {
        return userRepository.findBySchoolNum(schoolNum)
                .map(UserDetailDto::of)
                .orElseThrow(() -> new IllegalArgumentException("해당 학번을 가진 학생을 조회할 수 없습니다. "));
    }


    // 사용자 정보 입력(수정)
    @Transactional
    public IdResponse userModify(String token, UserModifyRequest request) {
        User user = findUserByToken(token);

        // 학교 가져오기 -> 리스트에서 선택
        School school = null;
        if (request.getSchoolName() != null && !request.getSchoolName().equals(user.getSchool().getName())) {
            school = schoolRepository.findByName(request.getSchoolName())
                    .orElseThrow(() -> new IllegalArgumentException("해당 학교가 존재하지 않습니다."));
            user.updateSchool(school);
        }

        user.update(
                request.getUsername(),
                request.getPhone(),
                request.getGitId(),
                request.getNotionMail(),
                request.getPlusMail(),
                request.getCollabTools(),
                request.getEmail(),
                request.getMajor(),
                request.getBirth(),
                request.getSns(),
                request.getInfo(),
                request.getSchoolNum()
        );
        return IdResponse.of(user);
    }


    //   UserModifyRequest(Long userId, String username, String info, String schoolName, String schoolNum, String phone, Long schoolId, String gitId, String notionMail, String plusMail, String email, String major, String birth, String sns, String collabTools) {


        // 회원가입
    @Transactional
    public IdResponse signUp(UserRegistDto request) {
        if (userRepository.existsByPhone(request.getPhone())) {
            throw new IllegalArgumentException("이미 가입되어 있는 전화번호입니다. ");
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("이미 가입되어 있는 이메일입니다. ");
        }

        School school = null;
        if (request.getSchoolName() != null) {
            school = schoolRepository.findByName(request.getSchoolName())
                .orElseThrow(() -> new IllegalArgumentException("해당 학교를 조회할 수 없습니다. "));

            if (userRepository.existsBySchoolAndSchoolNum(school, request.getSchoolNum())) {
                throw new IllegalArgumentException("이미 가입되어 있는 사용자(학번) 입니다. ");
            }
        }

        User user = request.toUser(school, bCryptPasswordEncoder.encode(request.getPassword()));
        userRepository.save(user);
        return IdResponse.of(user);
    }

    // 탈퇴
    // TODO: 회원 삭제 시 처리 추가
    @Transactional
    public void deleteUser(String token) {
        User user = findUserByToken(token);

        // 업무 자동 삭제
        userRepository.delete(user);
    }

    // 로그인

    // 로그아웃

    // userId 반환 함수
    public UserIdResponseDto getUserIdByToken(String token) {
        User user = findUserByToken(token);
        return UserIdResponseDto.builder()
                .username(user.getUsername())
                .userId(user.getId())
                .build();
    }

}

