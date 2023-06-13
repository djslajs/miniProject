package com.miniproject.controller;

import com.miniproject.request.Login;
import com.miniproject.response.SessionResponse;
import com.miniproject.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;

@RestController
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AuthService authService;

    @PostMapping("/auth/login")
    public ResponseEntity login(@RequestBody Login login) {
//        //json에서 ID 조회
//        log.info( ">>>login{}", login);
//
//        //DB조회
//        LoginUser user = userRepository.findByEmailAndPassword(login.getEmail(), login.getPassword())
//                .orElseThrow(InvalidSignInInformation::new);
//        //토큰 응답
//        return user;


//        String accessToken = authService.sinIn( login);
//        return new SessionResponse( accessToken);
        String accessToken = authService.sinIn( login);
        ResponseCookie cookie = ResponseCookie.from( "SESSION", accessToken)
                .domain( "localhost") // todo 서버 환경에 따른 분리 필요
                .path("/")
                .httpOnly( true)
                .secure( false)
                .maxAge( Duration.ofDays( 30))
                .sameSite("Strict")
                .build();
        log.info( ">>>>>>>>> cookie={}", cookie.toString());
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .build();
    }
}
