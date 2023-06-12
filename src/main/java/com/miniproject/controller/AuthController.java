package com.miniproject.controller;

import com.miniproject.request.Login;
import com.miniproject.response.SessionResponse;
import com.miniproject.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AuthService authService;

    @PostMapping("/auth/login")
    public SessionResponse login(@RequestBody Login login) {
//        //json에서 ID 조회
//        log.info( ">>>login{}", login);
//
//        //DB조회
//        LoginUser user = userRepository.findByEmailAndPassword(login.getEmail(), login.getPassword())
//                .orElseThrow(InvalidSignInInformation::new);
//        //토큰 응답
//        return user;
        String accessToken = authService.sinIn( login);
        return new SessionResponse( accessToken);
    }
}
