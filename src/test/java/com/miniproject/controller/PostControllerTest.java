package com.miniproject.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.miniproject.domain.Post;
import com.miniproject.repositiry.PostRepository;
import com.miniproject.request.PostCreate;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

//@WebMvcTest
@AutoConfigureMockMvc
@SpringBootTest
class PostControllerTest {

    @Autowired
    ObjectMapper objectMapper;

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
                .title( "제목입니다")
                .content("내용입니다")
                .build();
        String jsonData = objectMapper.writeValueAsString( postCreate);

        System.out.println( jsonData);
        // expected

        mockMvc.perform( MockMvcRequestBuilders.post( "/posts")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content( jsonData)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(""))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName( "/posts 요청시 title값을 필수다.")
    void test2() throws Exception {
        PostCreate postCreate = PostCreate.builder()
                .content("내용입니다")
                .build();
        String jsonData = objectMapper.writeValueAsString( postCreate);
        mockMvc.perform( MockMvcRequestBuilders.post( "/posts")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content( jsonData)
                )
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
//                .andExpect(MockMvcResultMatchers.content().string( "hello"))
                .andExpect(jsonPath( "$.code").value( "400"))
                .andExpect(jsonPath( "$.message").value( "잘못된 요청입니다."))
                .andExpect(jsonPath( "$.validationField.title").value( "타이틀을 입력해주세요."))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName( "/posts 요청시 DB값 저장.")
    void test3() throws Exception {
        PostCreate postCreate = PostCreate.builder()
                .title("제목입니다.")
                .content("내용입니다.")
                .build();
        String jsonData = objectMapper.writeValueAsString( postCreate);

        mockMvc.perform( MockMvcRequestBuilders.post( "/posts")
                .contentType(MediaType.APPLICATION_JSON)
                .content( jsonData)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
        // then
        Assertions.assertEquals( 1L, postRepository.count());
        Post post = postRepository.findAll().get( 0);
        Assertions.assertEquals( "제목입니다.", post.getTitle());
        Assertions.assertEquals( "내용입니다.", post.getContent());
    }

}