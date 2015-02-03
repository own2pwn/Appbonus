package com.appbonus.android.api.model;

public class LoginRequest {
    protected User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LoginRequest(String email, String password) {
        user = new User();
        user.setEmail(email);
        user.setPassword(password);
    }

    public static class User {
        protected String email;
        protected String password;

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }
}
