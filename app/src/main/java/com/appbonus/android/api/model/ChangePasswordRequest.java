package com.appbonus.android.api.model;

public class ChangePasswordRequest extends SimpleRequest {
    protected User user;

    public ChangePasswordRequest(String authToken) {
        super(authToken);
        user = new User();
    }

    public ChangePasswordRequest(String authToken, String currentPassword, String password) {
        this(authToken);
        user.setCurrentPassword(currentPassword);
        user.setPassword(password);
        user.setPasswordConfirmation(password);
    }

    static class User {
        protected String currentPassword;
        protected String password;
        protected String passwordConfirmation;

        public void setCurrentPassword(String currentPassword) {
            this.currentPassword = currentPassword;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public void setPasswordConfirmation(String passwordConfirmation) {
            this.passwordConfirmation = passwordConfirmation;
        }
    }
}
