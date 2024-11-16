package com.sfk.hodolog.repository;

import com.sfk.hodolog.domain.Post;
import com.sfk.hodolog.request.PostSearch;

import java.util.List;

public interface PostRepositoryCustom {

    List<Post> getList(PostSearch postSearch);
}
