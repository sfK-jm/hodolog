package com.sfk.hodolog.config;

import com.sfk.hodolog.domain.Users;
import lombok.Getter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.List;

public class UserPrincipal extends User {

    @Getter
    private final Long userId;
    private final String username;

    public UserPrincipal(Users users) {
        super(users.getEmail(), users.getPassword(),
                List.of(
                        new SimpleGrantedAuthority("ROLE_ADMIN")
                ));
        this.userId = users.getId();
        this.username = users.getName();
    }

    @Override
    public String getUsername() {
        return username;
    }
}
