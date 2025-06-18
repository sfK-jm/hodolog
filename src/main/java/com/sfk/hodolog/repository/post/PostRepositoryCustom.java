package com.sfk.hodolog.repository.post;

import com.sfk.hodolog.domain.Post;
import com.sfk.hodolog.request.post.PostSearch;
import org.springframework.data.domain.Page;

import java.util.List;

public interface PostRepositoryCustom {

    Page<Post> getList(PostSearch postSearch);
}
