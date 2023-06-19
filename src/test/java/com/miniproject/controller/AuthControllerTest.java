package com.miniproject.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.miniproject.domain.LoginUser;
import com.miniproject.domain.Session;
import com.miniproject.repositiry.SessionRepository;
import com.miniproject.repositiry.UserRepository;
import com.miniproject.request.Login;
import com.miniproject.request.SignUp;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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

    @Test
    @DisplayName("로그인 후 권한이 필요한 페이지 접속")
    void test5() throws Exception {
        //given
        LoginUser user = LoginUser.builder()
                .name( "cho")
                .email("asd@naver.com")
                .password("1234")
                .build();
        Session session = user.addSession();
        userRepository.save( user);
        mockMvc.perform( get( "/foo")
                .header( "Authorization", session.getAccessToken())
                .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("로그인 후 검증되지 않은 세션값으로 권한이 필요한 페이지에 접속 불가")
    void test6() throws Exception {
        //given
        LoginUser user = LoginUser.builder()
                .name( "cho")
                .email("asd@naver.com")
                .password("1234")
                .build();
        Session session = user.addSession();
        userRepository.save( user);
        mockMvc.perform( get( "/foo")
                .header( "Authorization", session.getAccessToken()+ "_other")
                .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isUnauthorized())
                .andDo(print());
    }

    @Test
    @DisplayName("회원가입")
    void test7() throws Exception {
        //given
        SignUp signUp = SignUp.builder()
                .name( "Cho")
                .password("1234")
                .email("djslajs@naver.com")
                .build();

        mockMvc.perform( post( "/auth/signup")
                .content(objectMapper.writeValueAsString( signUp))
                .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk())
                .andDo(print());
    }

//    @Test
//    @DisplayName("쿠키를 통한 권한이 필요한 페이지 접속")
//    void test7() throws Exception{
//
//        LoginUser user = LoginUser.builder()
//                .name( "cho")
//                .email("asd@naver.com")
//                .password("1234")
//                .build();
//        Session session = user.addSession();
//        userRepository.save( user);
//
//        ResponseCookie cookie = ResponseCookie.from( "SESSION", session.getAccessToken())
//                .domain( "localhost")
//                .path("/")
//                .httpOnly( true)
//                .secure( false)
//                .maxAge( Duration.ofDays( 30))
//                .sameSite("Strict")
//                .build();
//
//        mockMvc.perform( get( "/foo")
//                .contentType(MediaType.APPLICATION_JSON)
//                .cookie( cookie.getValue())
//        )
//                .andExpect(status().isOk())
//                .andDo(print());
//    }
}
