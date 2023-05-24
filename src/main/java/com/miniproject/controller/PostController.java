package com.miniproject.controller;

import com.miniproject.domain.Post;
import com.miniproject.request.PostCreate;
import com.miniproject.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.bind.BindResult;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping( "/posts")
    public void post(@RequestBody @Valid PostCreate request) {//, BindingResult result) {

        /**
         *  데이터 검증이유
         *  1. client 개발자가 실수로 값을 안보낼 수 있다.
         *  2. client bug로 인해 값이 누락될 수있다.
         *  3. 외부의 사람이 값을 임의로 조작해서 보낼 수 있다.
         *  4. DB에 값을 저장할 경우 의도치 않은 오류가 발생할 수 있다.
         *  5. 서버 개발자의 편안함을 위해
         */

        /**
         * 저장한 데이터 Entity -> reponse로 응답하기
         * 저장한 데이터의 primary_id -> response로 응답하기
         *                      ``Client에서는 수신한 id를 글 조회 API를 통해서 데이터를 수신받음
         *  응답 필요 없음 -> 클라이언트에서 모든 POST 데이터 context를 잘 관리함
         *  서버에서 유연하게 대응하는게 좋다( fix X) -> 한번에 잘 처리되는 케이스는 거의 없다. 잘 관리하는 형태로 하는것이 좋다.
         */
        postService.write( request);
//        Long postId = postService.write( request);
//        return Map.of( "postId", postId);
    }

    /**
     *  /posts -> 글 전체 조회( 검색+ 페이징)
     *  /posts/{postId} -> 글 한개만 조회
     */

    @GetMapping("/posts/{postId}")
    public Post get( @PathVariable(name = "postId") Long id) {
        Post post = postService.get( id);
        return post;
    }
}
