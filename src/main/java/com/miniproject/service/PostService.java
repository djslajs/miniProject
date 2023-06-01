package com.miniproject.service;

import com.miniproject.domain.Post;
import com.miniproject.domain.PostEditor;
import com.miniproject.exception.PostNotFound;
import com.miniproject.repositiry.PostRepository;
import com.miniproject.request.PostCreate;
import com.miniproject.request.PostSearch;
import com.miniproject.response.PostResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

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
                .orElseThrow(PostNotFound::new);
        return  PostResponse.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .build();
        /**
         *  Controller -> WebService(response를 위해서 사용하는 호출사용) - > Repository
         *                PostService(다른 서비스와 통신을 하기 위해 만든 서비스)
         *                Service는 레이어를 나눠서 작업하는게 좋을지 생각해보기
         */
    }

    /**
     * 글이 많을 경우 비용이 너무 많이 든다
     * DB가 뻗을 수 있다. timeover
     * DB -> 에플리케이션 서버로 전달하는시간, 트래픽 비용 등이 많이 발생할 수 있다.
     */
    public List<PostResponse> getList( PostSearch postSearch) {
        // web -> page 1 -> 0
        return postRepository.getList( postSearch).stream()
               .map(PostResponse::new)
               .collect( Collectors.toList());
    }

    @Transactional
    public void edit( Long id, PostEdit postEdit) {
        Post post = postRepository.findById( id)
                .orElseThrow( PostNotFound::new);

//        post.change( postEdit.getTitle(), postEdit.getContent());
        PostEditor.PostEditorBuilder editorBuilder = post.toEditor();

        PostEditor postEditor = editorBuilder.title( postEdit.getTitle())
                .content(postEdit.getContent())
                .build();
        post.edit( postEditor);

//        post.edit( postEdit.getTitle(), postEdit.getContent());
    }

    public void delete( Long id) {
        Post post = postRepository.findById( id)
                .orElseThrow( PostNotFound::new);

        postRepository.delete( post);
    }

}
