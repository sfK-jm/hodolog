package com.sfk.hodolog.repository;

import com.sfk.hodolog.domain.Users;
import org.springframework.data.repository.CrudRepository;

import javax.swing.text.html.Option;
import java.util.Optional;

public interface UserRepository extends CrudRepository<Users, Long> {
    Optional<Users> findByEmailAndPassword(String email, String password);

    Optional<Users> findByEmail(String email);
}
