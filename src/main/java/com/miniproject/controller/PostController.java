package com.miniproject.controller;

import com.miniproject.request.PostCreate;
import com.miniproject.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.bind.BindResult;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
    public Map<String, String> post(@RequestBody @Valid PostCreate request) {//, BindingResult result) {

        /**
         *  데이터 검증이유
         *  1. client 개발자가 실수로 값을 안보낼 수 있다.
         *  2. client bug로 인해 값이 누락될 수있다.
         *  3. 외부의 사람이 값을 임의로 조작해서 보낼 수 있다.
         *  4. DB에 값을 저장할 경우 의도치 않은 오류가 발생할 수 있다.
         *  5. 서버 개발자의 편안함을 위해
         */

        log.info( "params={}", request.toString());
//        if (result.hasErrors()) {
//            List<FieldError> fieldErrors = result.getFieldErrors();
//            FieldError firstFieldError = fieldErrors.get( 0);
//            String fieldName = firstFieldError.getField();
//            String errorMessage = firstFieldError.getDefaultMessage();
//            Map< String, String> error = new HashMap();
//            error.put( fieldName, errorMessage);
//            return error;
//        }

        postService.write( request);
        return Map.of();
    }
}