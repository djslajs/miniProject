package com.miniproject.controller;

import com.miniproject.config.AppConfig;
import com.miniproject.request.Login;
import com.miniproject.request.SignUp;
import com.miniproject.service.AuthService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AuthService authService;

    private final AppConfig appConfig;

    @PostMapping("/auth/signup")
    public void singUp( @RequestBody SignUp signUp) {
        authService.signUp(signUp);
    }
}
