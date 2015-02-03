package com.appbonus.android.api.model;

public class VkLoginRequest extends VkSimpleRequest {
    protected String mail;

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }
}
