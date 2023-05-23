package com.miniproject.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

@Setter
@Getter
@ToString
public class PostCreate {

    @NotBlank( message = "타이틀을 입력해주세요.")
    private final String title;

    @NotBlank( message = "컨텐츠를 입력해주세요.")
    private final String content;

    @Builder
    /**
     * 빌더의 장점
     * 1. 가독성이 좋음
     * 2. 값 생성에 대한 유연함
     * 3. 필요한 값만 받을 수 있다 -> 오버로딩 가능한 조건 찾아보기
     * 4. 객체의 불변성
     */
    public PostCreate(String title, String content) {
        this.title = title;
        this.content = content;
    }

//    public PostCreate changeTitle( String title) {
//        return PostCreate.builder().title( title).build();
//    }
}
