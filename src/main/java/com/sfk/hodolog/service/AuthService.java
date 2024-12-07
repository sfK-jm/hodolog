package com.sfk.hodolog.service;

import com.sfk.hodolog.domain.Session;
import com.sfk.hodolog.domain.Users;
import com.sfk.hodolog.exception.InvalidSigninInformation;
import com.sfk.hodolog.repository.UserRepository;
import com.sfk.hodolog.request.Login;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;

    @Transactional
    public String signin(Login login) {
        Users user = userRepository.findByEmailAndPassword(login.getEmail(), login.getPassword())
                .orElseThrow(InvalidSigninInformation::new);

        Session session = user.addSession();

        return session.getAccessToken();
    }
}
