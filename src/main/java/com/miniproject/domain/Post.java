package com.miniproject.domain;

import lombok.*;

import javax.persistence.*;

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
}
