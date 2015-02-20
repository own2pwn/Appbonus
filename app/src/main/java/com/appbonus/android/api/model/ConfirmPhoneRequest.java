package com.appbonus.android.api.model;

public class ConfirmPhoneRequest extends SimpleRequest {
    protected ConfirmPhoneRequest.User user;

    public ConfirmPhoneRequest(String authToken, String phoneCode) {
        super(authToken);
        user = new ConfirmPhoneRequest.User();
        user.phoneCode = phoneCode;
    }

    static class User {
        public String phoneCode;
    }
}
