package com.dolphin.net.exception;

public class UnauthorizedException extends RuntimeException {
    public static final String MESSAGE = "UnauthorizedException";

    public UnauthorizedException() {
        this(MESSAGE);
    }

    public UnauthorizedException(String detailMessage) {
        super(detailMessage);
    }
}
