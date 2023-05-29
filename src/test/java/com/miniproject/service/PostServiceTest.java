package com.miniproject.service;

import com.miniproject.domain.Post;
import com.miniproject.exception.PostNotFound;
import com.miniproject.repositiry.PostRepository;
import com.miniproject.request.PostCreate;
import com.miniproject.request.PostSearch;
import com.miniproject.response.PostResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

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

        List<Post> requestPost = IntStream.range( 1, 20)
                .mapToObj( i -> {
                    return Post.builder()
                            .title( "제목"+i)
                            .content( "내용"+i)
                            .build();
                })
                .collect( Collectors.toList());

        postRepository.saveAll( requestPost);
        //sql -> select, limit, offset
        PostSearch postSearch = PostSearch.builder()
//                .page(1)
//                .size(10)
                .build();
        //when
        List<PostResponse> posts = postService.getList( postSearch);

        //then
        Assertions.assertEquals( 19L, posts.size());
        Assertions.assertEquals( "제목19", posts.get(0).getTitle());
    }

    @Test
    @DisplayName( "게시글 수정")
    void test4() {
        Post post = Post.builder()
                .title( "제목")
                .content( "내용")
                .build();
        postRepository.save( post);

        PostEdit postEdit = PostEdit.builder()
                .title( "제목수정")
                .build();

        postService.edit( post.getId(), postEdit);
        //then

        Post postChange = postRepository.findById( post.getId())
                .orElseThrow(() -> new RuntimeException( "글이 존재하지 않습니다"));
        Assertions.assertEquals( "제목수정", postChange.getTitle());
    }

    @Test
    @DisplayName( "글 내용수정 수정")
    void test5() {
        Post post = Post.builder()
                .title( "제목")
                .content( "내용")
                .build();
        postRepository.save( post);

        PostEdit postEdit = PostEdit.builder()
                .title( null)
                .content( "내용수정")
                .build();

        postService.edit( post.getId(), postEdit);
        //then

        Post postChange = postRepository.findById( post.getId())
                .orElseThrow(() -> new RuntimeException( "글이 존재하지 않습니다"));
        Assertions.assertEquals( "제목", postChange.getTitle());
        Assertions.assertEquals( "내용수정", postChange.getContent());
    }

    @Test
    @DisplayName( "게시글 삭제")
    void test6() {
        Post post = Post.builder()
                .title( "제목")
                .content( "내용")
                .build();
        postRepository.save( post);
        //then

        postService.delete( post.getId());

        Assertions.assertEquals( 0, postRepository.count());
    }

    @Test
    @DisplayName( "글 한개 조회 - 존재 하지 않는글")
    void test7() {
        //given
        Post post = Post.builder()
                .title( "제목")
                .content( "내용")
                .build();
        postRepository.save( post);

        Long postId = post.getId();

        // expected
        Assertions.assertThrows( PostNotFound.class, () -> {
            postService.get( post.getId() +1L);
        });
    }

    @Test
    @DisplayName( "게시글 삭제 - 존재하지않는글")
    void test8() {
        Post post = Post.builder()
                .title( "제목")
                .content( "내용")
                .build();
        postRepository.save( post);
        // expected
        Assertions.assertThrows( PostNotFound.class, () -> {
            postService.delete( post.getId()+1L);
        });
    }

    @Test
    @DisplayName( "글 내용수정 수정 - 존재 하지 않는 글")
    void test9() {
        Post post = Post.builder()
                .title( "제목")
                .content( "내용")
                .build();
        postRepository.save( post);

        PostEdit postEdit = PostEdit.builder()
                .title( null)
                .content( "내용수정")
                .build();

        //expected
        Assertions.assertThrows( PostNotFound.class, () -> {
            postService.edit( post.getId()+1L, postEdit);
        });
    }
}