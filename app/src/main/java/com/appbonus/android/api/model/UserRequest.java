package com.appbonus.android.api.model;

import com.appbonus.android.model.User;

public class UserRequest extends SimpleRequest {
    protected User user;

    public UserRequest(String authToken, User user) {
        super(authToken);
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
