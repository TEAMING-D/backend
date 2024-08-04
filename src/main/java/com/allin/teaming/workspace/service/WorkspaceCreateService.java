package com.allin.teaming.workspace.service;


import com.allin.teaming.user.Jwt.JwtUtil;
import com.allin.teaming.user.domain.Membership;
import com.allin.teaming.user.domain.User;
import com.allin.teaming.user.repository.MembershipRepository;
import com.allin.teaming.user.repository.UserRepository;
import com.allin.teaming.workspace.domain.Workspace;
import com.allin.teaming.workspace.dto.MembershipDTO;
import com.allin.teaming.workspace.dto.WorkspaceCreateRequestDto;
import com.allin.teaming.workspace.dto.WorkspaceDTO;
import com.allin.teaming.workspace.dto.WorkspaceResponseDto;
import com.allin.teaming.workspace.repository.WorkspaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WorkspaceCreateService {

    private final WorkspaceRepository workspaceRepository;
    private final UserRepository userRepository;
    private final MembershipRepository membershipRepository;
    private final JwtUtil jwtUtil;

    private User findUserByToken(String token) {
        return userRepository.findByEmail(jwtUtil.getEmail(token.split(" ")[1]))
                .orElseThrow(() -> new IllegalArgumentException("해당 회원을 조회할 수 없습니다."));
    }

    private User findUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저를 찾을 수 없습니다. "));
    }

    // Workspace 생성
    @Transactional
    public WorkspaceResponseDto createWorkspace(String token, WorkspaceCreateRequestDto request) {
        User user = findUserByToken(token);
        Workspace workspace = request.toEntity();

        workspaceRepository.save(workspace);

        System.out.println("여긴되나?");
        List<Long> userIds = new ArrayList<>();
        if (request != null) {
            userIds.addAll(request.getMembers());
        }
        userIds.add(user.getId());

        List<MembershipDTO> membershipDTOS = new ArrayList<>();
        for (Long userId : userIds) {
            Membership membership = Membership.builder()
                    .user(findUserById(userId))
                    .workspace(workspace)
                    .build();
            membershipRepository.save(membership);
            membershipDTOS.add(MembershipDTO.toDto(membership));
        }
        return WorkspaceResponseDto.toDto(workspace, membershipDTOS);
    }

}
