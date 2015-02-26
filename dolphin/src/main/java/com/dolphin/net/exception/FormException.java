package com.dolphin.net.exception;

public class FormException extends RuntimeException {
    public String form;
    public String message;

    public FormException() {
    }

    @Override
    public String getMessage() {
        return message;
    }
}
