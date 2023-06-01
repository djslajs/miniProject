package com.miniproject.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.miniproject.domain.Post;
import com.miniproject.repositiry.PostRepository;
import com.miniproject.request.PostCreate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.PayloadDocumentation;
import org.springframework.restdocs.request.RequestDocumentation;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.validation.Payload;

import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.restdocs.snippet.Attributes.key;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs( uriScheme = "https", uriHost = "api.cho.com", uriPort = 443) // API문서의 기본 설정값
@ExtendWith(RestDocumentationExtension.class)
public class PostControllerDocTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private ObjectMapper objectMapper;

//    @BeforeEach
//    void setUp(WebApplicationContext webApplicationContext, RestDocumentationContextProvider restDocumentation) {
//        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
//                .apply(documentationConfiguration(restDocumentation))
//                .build();
//    }

    @Test
    @DisplayName( "테스트")
    void test1() throws Exception {
        Post post1 = Post.builder()
                .title("제목1")
                .content("내용1")
                .build();

        postRepository.save( post1);

        mockMvc.perform( RestDocumentationRequestBuilders.get("/posts/{postId}", 1L).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("post-inquiry",
                        pathParameters(
                                parameterWithName( "postId").description( "게시글 ID")
                        ),
                        responseFields(
                                fieldWithPath( "id").description( "게시글 ID"),
                                fieldWithPath( "title").description( "제목").attributes( key( "constraints").value( "제목 입력해주세요.")),
                                fieldWithPath( "content").description( "내용").optional()
                        )
                ));
    }

    @Test
    @DisplayName( "글 등록 테스트")
    void test2() throws Exception {

        PostCreate post1 = PostCreate.builder()
                .title("제목1")
                .content("내용1")
                .build();

        String jsonData = objectMapper.writeValueAsString( post1);
        mockMvc.perform( RestDocumentationRequestBuilders.post("/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content( jsonData))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("post-create",
                        requestFields(
                                fieldWithPath( "title").description( "제목"),
                                fieldWithPath( "content").description( "내용")
                        )
                ));
    }
}
