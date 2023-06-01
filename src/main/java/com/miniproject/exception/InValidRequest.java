package com.miniproject.exception;

import lombok.Getter;

/**
 * status -> 400
 */
@Getter
public class InValidRequest extends ChoException{
    private static final String MESSAGE = "잘못된 요청입니다.";
    public InValidRequest() {
        super(MESSAGE);
    }

    public InValidRequest( Throwable cause) {
        super(MESSAGE, cause);
    }

    public InValidRequest( String fieldName, String message) {
        super( MESSAGE);
        addValidation( fieldName, message);
    }

    @Override
    public int getStatusCode(){
        return 400;
    }
}
