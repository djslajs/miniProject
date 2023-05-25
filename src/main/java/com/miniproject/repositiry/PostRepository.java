package com.miniproject.repositiry;

import com.miniproject.controller.PostRepositoryCustom;
import com.miniproject.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long>, PostRepositoryCustom {
}
