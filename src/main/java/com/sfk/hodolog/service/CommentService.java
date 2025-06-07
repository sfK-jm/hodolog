package com.sfk.hodolog.service;

import com.sfk.hodolog.domain.Comment;
import com.sfk.hodolog.domain.Post;
import com.sfk.hodolog.exception.CommentNotFound;
import com.sfk.hodolog.exception.InvalidPassword;
import com.sfk.hodolog.exception.PostNotFound;
import com.sfk.hodolog.repository.comment.CommentRepository;
import com.sfk.hodolog.repository.post.PostRepository;
import com.sfk.hodolog.request.comment.CommentCreate;
import com.sfk.hodolog.request.comment.CommentDelete;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void write(Long postId, @Valid CommentCreate request) {
        Post post = postRepository.findById(postId)
                .orElseThrow(PostNotFound::new);

        String encryptedPassword = passwordEncoder.encode(request.getPassword());

        Comment comment = Comment.builder()
                .author(request.getAuthor())
                .password(encryptedPassword)
                .content(request.getContent())
                .build();

        post.addComment(comment);
    }

    public void delete(Long commentId, @Valid CommentDelete request) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(CommentNotFound::new);

        String encryptedPassword = comment.getPassword();
        if (!passwordEncoder.matches(request.getPassword(), encryptedPassword)) {
            throw new InvalidPassword();
        }

        commentRepository.delete(comment);
    }
}
