package com.miniproject.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.miniproject.domain.Post;
import com.miniproject.domain.PostEditor;
import com.miniproject.repositiry.PostRepository;
import com.miniproject.request.PostCreate;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

//@WebMvcTest
@SpringBootTest
@AutoConfigureMockMvc
class PostControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PostRepository postRepository;

    @BeforeEach
    void clean() {
        postRepository.deleteAll();
    }


    @Test
    @DisplayName( "글 작성 요청시 title값을 필수다.")
    void test2() throws Exception {
        PostCreate postCreate = PostCreate.builder()
                .content("내용입니다")
                .build();
        String jsonData = objectMapper.writeValueAsString( postCreate);
        mockMvc.perform( post( "/posts")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content( jsonData)
                )
                .andExpect(status().isBadRequest())
//                .andExpect(MockMvcResultMatchers.content().string( "hello"))
                .andExpect(jsonPath( "$.code").value( "400"))
                .andExpect(jsonPath( "$.message").value( "잘못된 요청입니다."))
                .andExpect(jsonPath( "$.validationField.title").value( "타이틀을 입력해주세요."))
                .andDo(print());
    }

    @Test
    @DisplayName( "/posts 요청시 DB값 저장.")
    void test3() throws Exception {
        PostCreate postCreate = PostCreate.builder()
                .title("제목입니다.")
                .content("내용입니다.")
                .build();
        String jsonData = objectMapper.writeValueAsString( postCreate);

        mockMvc.perform( post( "/posts?authorization={authorization}", "cho")
                .header( "authorization", "cho2")
                .contentType(MediaType.APPLICATION_JSON)
                .content( jsonData)
                )
                .andExpect(status().isOk())
                .andDo(print());
        // then
        Assertions.assertEquals( 1L, postRepository.count());
        Post post = postRepository.findAll().get( 0);
        Assertions.assertEquals( "제목입니다.", post.getTitle());
        Assertions.assertEquals( "내용입니다.", post.getContent());
    }

    @Test
    @DisplayName( "글 한개 조회")
    void test4() throws Exception {
        //given
        Post post = Post.builder()
                .title("123456789012345")
                .content("내용")
                .build();
        postRepository.save( post);
        //클라이언트 요청 - > title의 길이를 10글자로 제한


        //when

        mockMvc.perform( get( "/posts/{postId}", post.getId())
                .contentType(MediaType.APPLICATION_JSON))
//                .content( json))
                .andExpect( status().isOk())
                .andExpect( jsonPath("$.id").value( post.getId()))
                .andExpect( jsonPath("$.title").value( "1234567890"))
                .andExpect( jsonPath("$.content").value( post.getContent()))
                .andDo(print());
        //then
    }

    @Test
    @DisplayName( "글 여러개 조회")
    void test5() throws Exception {
        //given
        Post post1 = postRepository.save(
                Post.builder()
                .title("제목1")
                .content("내용1")
                .build()
        );

        Post post2 = postRepository.save(
                Post.builder()
                .title("제목2")
                .content("내용2")
                .build()
        );
        //클라이언트 요청 - > title의 길이를 10글자로 제한


        //when

        mockMvc.perform( get( "/posts?page=1&size=10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect( status().isOk())
                /**
                 * [{},{}, ....]
                 */
                .andExpect( jsonPath("$.length()", Matchers.is(2)))
                .andExpect( jsonPath("$[0].id").value( post2.getId()))
                .andExpect( jsonPath("$[0].title").value( "제목2"))
                .andExpect( jsonPath("$[0].content").value( "내용2"))
                .andExpect( jsonPath("$[1].id").value( post1.getId()))
                .andExpect( jsonPath("$[1].title").value( "제목1"))
                .andExpect( jsonPath("$[1].content").value( "내용1"))
                .andDo(print());
        //then
    }

    @Test
    @DisplayName( "페이징처리")
    void test6() throws Exception {
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
        //클라이언트 요청 - > title의 길이를 10글자로 제한


        //when

        mockMvc.perform( get( "/posts?page=1&size=10") // 정렬은 인덱스가 있는 것으로(속도문제)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect( status().isOk())
                .andExpect( jsonPath("$.length()", Matchers.is( 10)))
                .andExpect( jsonPath("$[0].title").value( "제목30"))
                .andExpect( jsonPath("$[0].content").value( "내용30"))
                .andDo(print());
        //then
    }

    @Test
    @DisplayName( "페이지를 0으로 요청하면 첫 페이지를 가져온다")
    void test7() throws Exception {
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
        //클라이언트 요청 - > title의 길이를 10글자로 제한


        //when

        mockMvc.perform( get( "/posts?page=0&size=10") // 정렬은 인덱스가 있는 것으로(속도문제)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect( status().isOk())
                .andExpect( jsonPath("$.length()", Matchers.is( 10)))
                .andExpect( jsonPath("$[0].title").value( "제목30"))
                .andExpect( jsonPath("$[0].content").value( "내용30"))
                .andDo(print());
        //then
    }

    @Test
    @DisplayName( "글 제목수정")
    void test8() throws Exception {
        //given
        Post post = Post.builder()
                .title( "제목")
                .content( "내용")
                .build();
        postRepository.save( post);
        //클라이언트 요청 - > title의 길이를 10글자로 제한

        PostEditor postEditor = PostEditor.builder()
                .title( "제목")
                .content( "내용수정")
                .build();

        mockMvc.perform( patch( "/posts/{postId}", post.getId()) // 정렬은 인덱스가 있는 것으로(속도문제)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content( objectMapper.writeValueAsString( postEditor))
                )
                .andExpect( status().isOk())
//                .andExpect( jsonPath("$.id").value( 1))
//                .andExpect( jsonPath("$.title").value( "제목"))
//                .andExpect( jsonPath("$.content").value( "내용수정"))
                .andDo(print());
        //then
    }

    @Test
    @DisplayName( "글 삭제")
    void test9() throws Exception {
        //given
        Post post = Post.builder()
                .title( "제목")
                .content( "내용")
                .build();
        postRepository.save( post);
        //클라이언트 요청 - > title의 길이를 10글자로 제한

        mockMvc.perform( delete( "/posts/{postId}", post.getId()) // 정렬은 인덱스가 있는 것으로(속도문제)
                .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect( status().isOk())
                .andDo(print());
        //then
    }

    @Test
    @DisplayName( "존재하지 않는글 조회")
    void test10() throws Exception {

        //expected
        mockMvc.perform( delete( "/posts/{postId}", 1L) // 정렬은 인덱스가 있는 것으로(속도문제)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect( status().isNotFound())
                .andDo(print());
    }

    @Test
    @DisplayName( "존재하지 게시글 수정")
    void test11() throws Exception {

        //expected
        PostEditor postEditor = PostEditor.builder()
                .title( "제목")
                .content( "내용수정")
                .build();

        mockMvc.perform( patch( "/posts/{postId}", 1L) // 정렬은 인덱스가 있는 것으로(속도문제)
                .content( objectMapper.writeValueAsString( postEditor)) // 정렬은 인덱스가 있는 것으로(속도문제)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect( status().isNotFound())
                .andDo(print());
    }

    @Test
    @DisplayName( "게시글 작성시 바보는 포함될 수 없다.")
    void test12() throws Exception {
        PostCreate postCreate = PostCreate.builder()
                .title("나는 바보입니다.")
                .content("내용입니다.")
                .build();
        String jsonData = objectMapper.writeValueAsString( postCreate);

        mockMvc.perform( post( "/posts")
                .contentType(MediaType.APPLICATION_JSON)
                .content( jsonData)
        )
                .andExpect(status().isBadRequest())
                .andDo(print());
        // then
    }


    /**
     *
     * API 문서 생서
     *
     * GET /posts/{postId} -> 단건조회
     * POST /posts -> 게시글 등록
     *
     * 클라이언트 입장 어떤 API 있는지 ㅁ름
     *
     * Spring RestDocs
     * - 운영코드에 -> 영향
     * - 코드 수정 -> 문서를 수정 x -> 코드(기능) <-> 문서
     * - Test 케이스실행 -> 문서를 생성해 준다.
     */

}