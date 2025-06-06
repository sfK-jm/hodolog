package com.sfk.hodolog.repository.post;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sfk.hodolog.domain.Post;
import com.sfk.hodolog.domain.QPost;
import com.sfk.hodolog.request.post.PostSearch;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class PostRepositoryImpl implements PostRepositoryCustom{

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Post> getList(PostSearch postSearch) {
        return jpaQueryFactory.selectFrom(QPost.post)
                .limit(postSearch.getSize())
                .offset(postSearch.getOffset())
                .orderBy(QPost.post.id.desc())
                .fetch();
    }
}
