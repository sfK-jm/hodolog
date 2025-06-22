package com.sfk.hodolog.service;

import com.sfk.hodolog.domain.Users;
import com.sfk.hodolog.exception.UserNotFound;
import com.sfk.hodolog.repository.UserRepository;
import com.sfk.hodolog.response.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public UserResponse getUserProfile(Long userId) {
        Users user = userRepository.findById(userId)
                .orElseThrow(UserNotFound::new);

        return new UserResponse(user);
    }
}
