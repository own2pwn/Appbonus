package com.appbonus.android.model.api;

import com.appbonus.android.model.User;

import java.io.Serializable;

public class LoginWrapper implements Serializable {
    protected User user;
    protected String authToken;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }
}
