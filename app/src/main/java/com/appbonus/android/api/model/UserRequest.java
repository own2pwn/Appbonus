package com.appbonus.android.api.model;

import com.appbonus.android.model.User;

public class UserRequest extends SimpleRequest {
    protected User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
