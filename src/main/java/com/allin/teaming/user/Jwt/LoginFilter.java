package com.allin.teaming.user.Jwt;

import com.allin.teaming.user.dto.CustomUserDetails;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

@RequiredArgsConstructor
public class LoginFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    @Override
    protected String obtainUsername(HttpServletRequest request) {
        return request.getParameter("email");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        String email = null;
        String password = null;

        if ("application/json".equals(request.getContentType())) {
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                Map<String, String> jsonRequest = objectMapper.readValue(request.getInputStream(), new TypeReference<Map<String, String>>() {});
                email = jsonRequest.get("email");
                password = jsonRequest.get("password");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            email = obtainUsername(request);
            password = obtainPassword(request);
        }

        System.out.println(email);

        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(email, password, null);
        System.out.println("loginfilter");
        return authenticationManager.authenticate(authToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        System.out.println("login success");
        CustomUserDetails customUserDetails = (CustomUserDetails) authResult.getPrincipal();
        String username = customUserDetails.getUsername();

        Collection<? extends GrantedAuthority> authorities = authResult.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();

        String role = auth.getAuthority();

        String token = jwtUtil.createJwt(username, role, 60 * 60 * 10L);

        System.out.println("여기까지 되나? 헤더 왜 안들어가 시발");
        System.out.println(token);
        response.addHeader("Authorization", "Bearer " + token);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        System.out.println("login failed");
        response.setStatus(401);
    }
}

