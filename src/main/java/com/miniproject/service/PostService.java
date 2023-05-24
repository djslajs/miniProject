package com.miniproject.service;

import com.miniproject.domain.Post;
import com.miniproject.repositiry.PostRepository;
import com.miniproject.request.PostCreate;
import com.miniproject.response.PostResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    public Long write(PostCreate postCreate){
        Post post = Post.builder()
                .title(postCreate.getTitle())
                .content(postCreate.getContent())
                .build();

//        return postRepository.save( post);
        postRepository.save( post);
        return post.getId();
    }

    public PostResponse get( Long id) {
        Post post = postRepository.findById( id)
                .orElseThrow(() -> new IllegalArgumentException( "존재하지 않는 글입니다"));
        PostResponse response =  PostResponse.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .build();
        /**
         *  Controller -> WebService(response를 위해서 사용하는 호출사용) - > Repository
         *                PostService(다른 서비스와 통신을 하기 위해 만든 서비스)
         *                Service는 레이어를 나눠서 작업하는게 좋을지 생각해보기
         */
        return response;
    }
}
