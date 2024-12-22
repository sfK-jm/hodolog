package com.sfk.hodolog.config;

import com.sfk.hodolog.domain.Users;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;
import java.util.List;

public class UserPrincipal extends User {

    private final Long userId;

    public UserPrincipal(Users users) {
        super(users.getEmail(), users.getPassword(), List.of(new SimpleGrantedAuthority("ADMIN")));
        this.userId = users.getId();
    }

    public Long getUserId() {
        return userId;
    }
}
