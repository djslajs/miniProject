package com.miniproject.service;

import com.miniproject.domain.LoginUser;
import com.miniproject.domain.Session;
import com.miniproject.exception.InvalidSignInInformation;
import com.miniproject.repositiry.UserRepository;
import com.miniproject.request.Login;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;

    @Transactional
    public String sinIn(Login login) {
        LoginUser user = userRepository.findByEmailAndPassword(login.getEmail(), login.getPassword())
                .orElseThrow(InvalidSignInInformation::new);
        Session session = user.addSession();

        return session.getAccessToken();
    }
}
