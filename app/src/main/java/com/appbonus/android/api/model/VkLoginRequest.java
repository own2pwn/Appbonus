package com.appbonus.android.api.model;

public class VkLoginRequest {
    protected User user;

    public VkLoginRequest() {
        user = new User();
    }

    public VkLoginRequest(String vkToken, String email) {
        this(vkToken);
        user.setEmail(email);
    }

    public VkLoginRequest(String vkToken) {
        this();
        user.setVkToken(vkToken);
    }

    static class User {
        protected String vkToken;
        protected String email;

        public String getVkToken() {
            return vkToken;
        }

        public void setVkToken(String vkToken) {
            this.vkToken = vkToken;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }
    }
}
