package com.miniproject.controller;

import com.miniproject.config.AppConfig;
import com.miniproject.domain.Session;
import com.miniproject.request.Login;
import com.miniproject.response.SessionResponse;
import com.miniproject.service.AuthService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.crypto.SecretKey;
import java.security.Key;
import java.time.Duration;
import java.util.Base64;

@RestController
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AuthService authService;

    private final AppConfig appConfig;

    @PostMapping("/auth/login")
    public SessionResponse login(@RequestBody Login login) {
        Long userId = authService.sinIn( login);

        SecretKey key = Keys.hmacShaKeyFor( Base64.getDecoder().decode( appConfig.KEY));
        String jws = Jwts.builder()
                .setSubject(String.valueOf( userId))
                .signWith(key)
                .compact();
        return new SessionResponse( jws);
    }
}
