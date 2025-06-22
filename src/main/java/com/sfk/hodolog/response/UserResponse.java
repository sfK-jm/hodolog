package com.sfk.hodolog.response;

import com.sfk.hodolog.domain.Users;
import lombok.Getter;

@Getter
public class UserResponse {

    private final Long id;
    private final String name;

    public UserResponse(Users user) {
        this.id = user.getId();
        this.name = user.getName();
    }
}
