package com.appbonus.android.model;

import java.io.Serializable;

public class Initiator implements Serializable {
    protected Long id;
    protected String email;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
