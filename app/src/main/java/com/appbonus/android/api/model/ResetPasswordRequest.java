package com.appbonus.android.api.model;

public class ResetPasswordRequest extends LoginRequest {
    public ResetPasswordRequest(String mail) {
        super();
        user.setEmail(mail);
    }
}
