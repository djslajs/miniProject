package com.miniproject.exception;

public class UnAuthorized extends ChoException{

    private static final String MESSAGE = "인증이필요합니다.";

    public UnAuthorized() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 401;
    }
}
