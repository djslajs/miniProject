package com.miniproject.service;

import com.miniproject.domain.Post;
import com.miniproject.repositiry.PostRepository;
import com.miniproject.request.PostCreate;
import com.miniproject.response.PostResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.data.domain.Sort.Direction.*;

@SpringBootTest
class PostServiceTest {

    @Autowired
    private PostService postService;

    @Autowired
    private PostRepository postRepository;

    @BeforeEach
    void clean() {
        postRepository.deleteAll();
    }

    @Test
    @DisplayName( "글 작성")
    void test1() {
        //given
        PostCreate post = PostCreate.builder()
                .title( "제목입니다.")
                .content( "내용입니다.")
                .build();

        //when
        postService.write( post);
        //then
        assertEquals( 1L, postRepository.count());
        assertEquals( "제목입니다.", post.getTitle());
        assertEquals( "내용입니다.", post.getContent());
    }

    @Test
    @DisplayName( "글 한개 조회")
    void test2() {
        //given
        Post requestPost = Post.builder()
                .title( "제목")
                .content( "내용")
                .build();
        postRepository.save( requestPost);

        Long postId = requestPost.getId();

        //when
        PostResponse post = postService.get( postId);

        //then
        Assertions.assertNotNull( post);
        Assertions.assertEquals( 1L, postRepository.count());
        Assertions.assertEquals( "제목",post.getTitle());
        Assertions.assertEquals( "내용",post.getContent());
    }

    @Test
    @DisplayName( "1페이지 조회")
    void test3() {
        //given

        List<Post> requestPost = IntStream.range( 1, 31)
                .mapToObj( i -> {
                    return Post.builder()
                            .title( "제목"+i)
                            .content( "내용"+i)
                            .build();
                })
                .collect( Collectors.toList());

        postRepository.saveAll( requestPost);
        //sql -> select, limit, offset
        Pageable pageable = PageRequest.of( 0, 5, Sort.by( DESC, "id"));
        //when
        List<PostResponse> posts = postService.getList( pageable);

        //then
        Assertions.assertEquals( 5L, posts.size());
        Assertions.assertEquals( "제목30", posts.get(0).getTitle());
        Assertions.assertEquals( "제목26", posts.get(4).getTitle());
    }
}