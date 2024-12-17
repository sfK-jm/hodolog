package com.sfk.hodolog.service;

import com.sfk.hodolog.crypto.PasswordEncoder;
import com.sfk.hodolog.crypto.ScryptPasswordEncoder;
import com.sfk.hodolog.domain.Users;
import com.sfk.hodolog.exception.AlreadyExistsEmailException;
import com.sfk.hodolog.exception.InvalidSigninInformation;
import com.sfk.hodolog.repository.UserRepository;
import com.sfk.hodolog.request.Login;
import com.sfk.hodolog.request.Signup;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder encoder;

    public void signup(Signup signup) {
        Optional<Users> usersOptional = userRepository.findByEmail(signup.getEmail());

        if (usersOptional.isPresent()) {
            throw new AlreadyExistsEmailException();
        }

        String encryptedPassword = encoder.encrypt(signup.getPassword());

        Users user = Users.builder()
                .name(signup.getName())
                .password(encryptedPassword)
                .email(signup.getEmail())
                .build();

        userRepository.save(user);
    }
}
