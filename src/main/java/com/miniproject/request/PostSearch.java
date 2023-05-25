package com.miniproject.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class PostSearch {
    @Builder.Default
    private Integer page = 1;

    @Builder.Default
    private Integer size = 2000;

    public long getOffset() {
        return (long) (Math.max( page -1, 0)) * Math.min(size, 2000);
    }
}
