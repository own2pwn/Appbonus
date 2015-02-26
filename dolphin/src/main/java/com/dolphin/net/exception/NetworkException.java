package com.dolphin.net.exception;

public class NetworkException extends RuntimeException {
    public NetworkException() {
        this("No network");
    }

    public NetworkException(String detailMessage) {
        super(detailMessage);
    }
}
