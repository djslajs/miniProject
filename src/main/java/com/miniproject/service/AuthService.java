package com.miniproject.service;

import com.miniproject.crypto.PasswordEncoder;
import com.miniproject.crypto.ScryptPasswordEncoder;
import com.miniproject.domain.LoginUser;
import com.miniproject.exception.AlreadyExistsEmailException;
import com.miniproject.exception.InvalidSignInInformation;
import com.miniproject.repositiry.UserRepository;
import com.miniproject.request.Login;
import com.miniproject.request.SignUp;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;

    private final PasswordEncoder encoder;

    public void signUp(SignUp signUp) {
        Optional<LoginUser> userOptional = userRepository.findByEmail( signUp.getEmail());
        if( userOptional.isPresent()) {
            throw new AlreadyExistsEmailException();
        }

        String encryptedPassword = encoder.encrypt( signUp.getPassword());
        LoginUser user = LoginUser.builder()
                .name(signUp.getName())
                .password( encryptedPassword)
                .email( signUp.getEmail())
                .build();
        userRepository.save( user);
    }
}
