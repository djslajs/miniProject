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

    @Test
    @DisplayName( "로그인 성공")
    void test3() {
        // given
        ScryptPasswordEncoder encoder = new ScryptPasswordEncoder();
        String encryptPassword = encoder.encrypt( "1234");
        LoginUser user = LoginUser.builder()
                .name( "Cho")
                .password( encryptPassword)
                .email("djslajs@naver.com")
                .build();
        userRepository.save( user);

        Login login = Login.builder()
                .email( "djslajs@naver.com")
                .password( "1234")
                .build();
        // when

        Long userId = authService.singIn( login);
        // then
        Assertions.assertNotNull( userId);
    }

    @Test
    @DisplayName( "비밀번호 틀림")
    void test4() {
        // given
        SignUp signUp = SignUp.builder()
                .name( "Cho")
                .password("1234")
                .email("djslajs@naver.com")
                .build();
        authService.signUp( signUp);

        Login login = Login.builder()
                .email( "djslajs@naver.com")
                .password( "12345")
                .build();
        // expected
        Assertions.assertThrows(InvalidSignInInformation.class, () -> authService.singIn( login));
    }
}