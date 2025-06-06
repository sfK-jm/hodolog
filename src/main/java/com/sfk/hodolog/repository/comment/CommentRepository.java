package com.sfk.hodolog.repository.comment;

import com.sfk.hodolog.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
