package com.appbonus.android.api.model;

public class ChangePasswordRequest {
    protected User user;

    public ChangePasswordRequest() {
        user = new User();
    }

    public ChangePasswordRequest(String currentPassword, String password) {
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
