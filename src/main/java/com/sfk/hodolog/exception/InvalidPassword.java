package com.sfk.hodolog.exception;

public class InvalidPassword extends HodologException {

    private static final String MESSAGE = "비밀번호가 올바르지 않습니다.";

    public InvalidPassword() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 400;
    }
}
