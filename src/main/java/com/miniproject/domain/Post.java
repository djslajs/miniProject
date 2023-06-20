package com.miniproject.domain;

import com.miniproject.service.PostEdit;
import lombok.*;

import jakarta.persistence.*;

@Entity
@NoArgsConstructor( access = AccessLevel.PUBLIC)
@Getter
//@Setter
public class Post {
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Lob
    private String content;

    @Builder
    public Post(String title, String content) {
        this.title = title;
        this.content = content;
    }
    // 서비스의 정책을 최대한 넣지 않는 편이 좋다.
//    public String getTitle() {
//
//        return this.title.substring( 0, 10);
//    }

    public void change( String title, String content) {
        this.title = title;
        this.content = content;
    }

    public PostEditor.PostEditorBuilder toEditor() {
         return PostEditor.builder()
                .title( title)
                .content( content);
    }

    public void edit( String title, String content) {
        this.title = title;
        this.content = content;
    }

    public void edit(PostEditor postEditor) {
        title = postEditor.getTitle();
        content = postEditor.getContent();
    }
}
