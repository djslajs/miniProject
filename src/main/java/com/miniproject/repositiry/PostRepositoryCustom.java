package com.miniproject.repositiry;

import com.miniproject.domain.Post;
import com.miniproject.request.PostSearch;

import java.util.List;

public interface PostRepositoryCustom {

    List<Post> getList( PostSearch postSearch);
}
