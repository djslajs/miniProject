package com.miniproject.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.miniproject.domain.LoginUser;
import com.miniproject.repositiry.SessionRepository;
import com.miniproject.repositiry.UserRepository;
import com.miniproject.request.Login;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    UserRepository userRepository;

    @Autowired
    SessionRepository sessionRepository;

    @BeforeEach
    void clean() {
        userRepository.deleteAll();
    }

    @Test
    @DisplayName( "로그인 성공.")
    void test2() throws Exception {
        //given
        userRepository.save(LoginUser.builder()
                .name( "cho")
                .email("asd@naver.com")
                .password("1234")
                .build());


        Login login = Login.builder()
                .email( "asd@naver.com")
                .password( "1234")
                .build();



        String jsonData = objectMapper.writeValueAsString( login);
        mockMvc.perform( post( "/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content( jsonData)
        )
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @Transactional
    @DisplayName( "로그인 성공 후 세션 1개생성.")
    void test3() throws Exception {
        //given
        LoginUser user = userRepository.save(LoginUser.builder()
                .name( "cho")
                .email("asd@naver.com")
                .password("1234")
                .build());

        Login login = Login.builder()
                .email( "asd@naver.com")
                .password( "1234")
                .build();

        String jsonData = objectMapper.writeValueAsString( login);
        mockMvc.perform( post( "/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content( jsonData)
        )
                .andExpect(status().isOk())
                .andDo(print());
//        LoginUser loggedInUser = userRepository.findById( user.getId())
//                .orElseThrow(RuntimeException::new);
        Assertions.assertEquals( 1L, user.getSessions().size());
    }

    @Test
//    @Transactional
    @DisplayName( "로그인 성공 후 세션 응답.")
    void test4() throws Exception {
        //given
        LoginUser user = userRepository.save(LoginUser.builder()
                .name( "cho")
                .email("asd@naver.com")
                .password("1234")
                .build());

        Login login = Login.builder()
                .email( "asd@naver.com")
                .password( "1234")
                .build();

        String jsonData = objectMapper.writeValueAsString( login);
        mockMvc.perform( post( "/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content( jsonData)
        )
                .andExpect(status().isOk())
                .andExpect( jsonPath("$.accessToken", Matchers.notNullValue()))
                .andDo(print());
    }
}
