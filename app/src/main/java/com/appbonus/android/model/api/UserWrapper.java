package com.appbonus.android.model.api;

import com.appbonus.android.model.User;

import java.io.Serializable;

public class UserWrapper implements Serializable {
    protected User user;

    public UserWrapper(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
