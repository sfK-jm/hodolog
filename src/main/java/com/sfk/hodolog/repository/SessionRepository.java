package com.sfk.hodolog.repository;

import com.sfk.hodolog.domain.Session;
import org.springframework.data.repository.CrudRepository;

public interface SessionRepository extends CrudRepository<Session, Long> {
}
