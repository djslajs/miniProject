package com.miniproject.controller;

import com.miniproject.domain.LoginUser;
import com.miniproject.exception.InValidRequest;
import com.miniproject.exception.InvalidSignInInformation;
import com.miniproject.repositiry.UserRepository;
import com.miniproject.request.Login;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final UserRepository userRepository;

    @PostMapping("/auth/login")
    public LoginUser login(@RequestBody Login login) {
        //json에서 ID 조회
        log.info( ">>>login{}", login);

        //DB조회
        LoginUser user = userRepository.findByEmailAndPassword(login.getEmail(), login.getPassword())
                .orElseThrow(InvalidSignInInformation::new);
        //토큰 응답
        return user;
    }
}
