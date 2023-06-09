package com.miniproject.service;

import com.miniproject.crypto.ScryptPasswordEncoder;
import com.miniproject.domain.LoginUser;
import com.miniproject.exception.AlreadyExistsEmailException;
import com.miniproject.exception.InvalidSignInInformation;
import com.miniproject.repositiry.UserRepository;
import com.miniproject.request.Login;
import com.miniproject.request.SignUp;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertThrows;

@ActiveProfiles("test")
@SpringBootTest
class AuthServiceTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthService authService;


    @AfterEach
    void clean() {
        userRepository.deleteAll();
    }

    @Test
    @DisplayName( "회원가입 성공")
    void test1() {
        // given
        SignUp signUp = SignUp.builder()
                .name( "Cho")
                .password("1234")
                .email("djslajs@naver.com")
                .build();
        // when
        authService.signUp( signUp);
        // then
        Assertions.assertEquals( 1L, userRepository.count());
        LoginUser user = userRepository.findAll().iterator().next();

        Assertions.assertEquals( "djslajs@naver.com", user.getEmail());
        Assertions.assertEquals( "1234", user.getPassword());
        Assertions.assertNotNull( user.getPassword());
//        Assertions.assertTrue( encoder.matches( "1234", user.getPassword()));
        Assertions.assertEquals( "Cho", user.getName());
    }

    @Test
    @DisplayName( "중복이메일 체크")
    void test2() {
        LoginUser user = LoginUser.builder()
                .email( "djslajs@naver.com")
                .password("1234")
                .name("whwh").build();
        userRepository.save( user);
        // given
        SignUp signUp = SignUp.builder()
                .name( "Cho")
                .password("1234")
                .email("djslajs@naver.com")
                .build();
        // when
        assertThrows(AlreadyExistsEmailException.class, () -> authService.signUp( signUp));

        // then
    }
}