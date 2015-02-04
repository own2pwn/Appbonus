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
    }

    static class User {
        protected String currentPassword;
        protected String password;

        public String getCurrentPassword() {
            return currentPassword;
        }

        public void setCurrentPassword(String currentPassword) {
            this.currentPassword = currentPassword;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }
}
