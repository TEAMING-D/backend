package com.allin.teaming.Jwt;

import com.allin.teaming.Domain.User.RoleType;
import com.allin.teaming.Domain.User.User;
import com.allin.teaming.Dto.User.CustomUserDetails;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorization = request.getHeader("Authorization");

        System.out.println("여기까지는 됨!");
        if (authorization == null || !authorization.startsWith("Bearer ")) {
            System.out.println("token null");

            filterChain.doFilter(request, response);
            return;
        }
        String token = authorization.split(" ")[1];

        if (jwtUtil.isExpired(token)) {
            System.out.println("token expired");

            filterChain.doFilter(request, response);
            return;
        }
        System.out.println("여기는??");

        String email = jwtUtil.getEmail(token);
        String roleString = jwtUtil.getRole(token);
        RoleType role = RoleType.valueOf(roleString.replace("ROLE_", ""));

        User user = User.builder()
                .email(email)
                .role(role)
                .password("temp")
                .username("temp")
                .build();
        System.out.println("2222");
        CustomUserDetails customUserDetails = new CustomUserDetails(user);

        // 스프링 시큐리티 인증 토큰 생성
        Authentication authToken = new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());

        // 세션에 사용자 등록
        SecurityContextHolder.getContext().setAuthentication(authToken);

        // 작업 완료 -> 다음필터로 넘기기
        filterChain.doFilter(request, response);
    }
}
