package com.dolphin.net.exception;

public class UnauthorizedException extends RuntimeException {

    public UnauthorizedException() {
        this("UnauthorizedException");
    }

    public UnauthorizedException(String detailMessage) {
        super(detailMessage);
    }
}
