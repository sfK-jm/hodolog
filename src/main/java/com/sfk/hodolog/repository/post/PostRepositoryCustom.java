package com.sfk.hodolog.repository.post;

import com.sfk.hodolog.domain.Post;
import com.sfk.hodolog.request.post.PostSearch;

import java.util.List;

public interface PostRepositoryCustom {

    List<Post> getList(PostSearch postSearch);
}
