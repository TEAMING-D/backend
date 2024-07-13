package com.allin.teaming.Controller;

import com.allin.teaming.Controller.User.UserController;
import com.allin.teaming.Domain.User.User;
import com.allin.teaming.Dto.User.UserDto.*;
import com.allin.teaming.Service.User.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
public class UserControllerTest {
    private MockMvc mvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    User user1 = getUser("user1", "010-1111-1111", "AAAA", "email1@email.com", "IT공학전공");
    User user2 = getUser("user2", "010-2222-2222", "BBBB", "email2@email.com", "IT공학전공");
    User user3 = getUser("user3", "010-3333-3333", "CCCC", "email3@email.com", "IT공학전공");
    User user4 = getUser("user4", "010-4444-4444", "DDDD", "email4@email.com", "IT공학전공");
    User user5 = getUser("user5", "010-5555-5555", "EEEE", "email5@email.com", "IT공학전공");
    List<User> users = new ArrayList<>();
    List<UserInfoDto> userDtos = new ArrayList<>();


    @BeforeEach
    void setUp() {
        // (Mock, InjectMocks 등 어노테이션 붙은 객체 초기화)
        // UserService 초기화
        MockitoAnnotations.openMocks(this);
        mvc = MockMvcBuilders.standaloneSetup(new UserController(userService)).build();
        users = Arrays.asList(user1, user2, user3, user4, user5);
        userDtos = users.stream().map(UserInfoDto::of).collect(Collectors.toList());

    }

    public String toJsonString(User user) throws JsonProcessingException {
        return objectMapper.writeValueAsString(user);
    }

    @Test
    @DisplayName("아이디로 단일 조회")
    public void getUserByIdTest() throws Exception {
        //given
        given(userService.getUserInfoById(any())).willReturn(UserInfoDto.of(user1));

        //when
        ResultActions actions = mvc.perform(get("/user/1"));

        //then
        actions.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.username").value("user1"));
    }

    @Test
    void getAllUserTest() throws Exception {
        when(userService.getAllUserInfo()).thenReturn(userDtos);

        mvc.perform(get("/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].username").value("user1"))
                .andExpect(jsonPath("$[0].phone").value("010-1111-1111"))
                .andExpect(jsonPath("$[0].info").value("AAAA"))
                .andExpect(jsonPath("$[0].email").value("email1@email.com"))
                .andExpect(jsonPath("$[0].major").value("IT공학전공"));
    }

    private User getUser(String username, String phone, String info,
                         String email, String major) {
        return User.builder()
                .username(username)
                .phone(phone)
                .info(info)
                .email(email)
                .major(major)
                .build();
    }
}
