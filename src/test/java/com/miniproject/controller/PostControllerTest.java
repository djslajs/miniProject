package com.miniproject.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.miniproject.domain.Post;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
    @DisplayName( "/posts 요청시 hello 출력")
    void test() throws Exception {
        // given
        PostCreate postCreate = PostCreate.builder()
                .title( "제목입니다.")
                .content("내용입니다.")
                .build();
        String jsonData = objectMapper.writeValueAsString( postCreate);

        System.out.println( jsonData);
        // expected

        mockMvc.perform( post( "/posts")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content( jsonData)
                )
                .andExpect(status().isOk())
                .andExpect(content().string(""))
                .andDo(print());
    }

    @Test
    @DisplayName( "/posts 요청시 title값을 필수다.")
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

        mockMvc.perform( post( "/posts")
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
        Post post1 = Post.builder()
                .title("123456789012345")
                .content("내용")
                .build();
        postRepository.save( post1);

        Post post2 = Post.builder()
                .title("123456789012345")
                .content("내용")
                .build();
        postRepository.save( post2);
        //클라이언트 요청 - > title의 길이를 10글자로 제한


        //when

        mockMvc.perform( get( "/posts")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect( status().isOk())
                /**
                 * [{},{}, ....]
                 */
                .andExpect( jsonPath("$.length()", Matchers.is(2)))
                .andDo(print());
        //then
    }

}