package com.appbonus.android.api.model;

public class ConfirmPhoneRequest {
    protected ConfirmPhoneRequest.User user;

    public ConfirmPhoneRequest(String phoneCode) {
        user = new ConfirmPhoneRequest.User();
        user.phoneCode = phoneCode;
    }

    static class User {
        public String phoneCode;
    }
}
